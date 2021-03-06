package com.tfr.microbrew.processor;

import com.tfr.microbrew.compare.CarbonationPriorityComparator;
import com.tfr.microbrew.config.BrewStep;
import com.tfr.microbrew.config.Constants;
import com.tfr.microbrew.config.DayOfWeek;
import com.tfr.microbrew.model.Batch;
import com.tfr.microbrew.service.BatchService;
import com.tfr.microbrew.service.InventoryService;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.tfr.microbrew.config.Constants.BrewHouse.CARBONATION_VESSELS;

/**
 *
 *
 * Created by Erik on 4/28/2017.
 */
@Component("TransferProcessor")
public class TransferProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private BatchService batchService;
    private InventoryService inventoryService;
    private CarbonationPriorityComparator carbonationPriorityComparator;

    private final Supplier<SortedSet<Batch>> sortedSetSupplierCarbonation =
            () -> new TreeSet<>(carbonationPriorityComparator);

    @Autowired
    public TransferProcessor(BatchService batchService,
                             InventoryService inventoryService,
                             CarbonationPriorityComparator carbonationPriorityComparator) {
        this.batchService = batchService;
        this.inventoryService = inventoryService;
        this.carbonationPriorityComparator = carbonationPriorityComparator;
    }

    @Override
    public void process(LocalDate date) {
        logger.debug("Transfer Day");
        //mark all completed carbonation as ready for storage
        batchService.getByStep(BrewStep.CARBONATE).stream()
                .filter(b -> b.getDaysInStep() >= b.getRecipe().getCarbonationDays())
                .forEach(batch -> {
                    batch.setCurrentStep(BrewStep.PACKAGE);
                    batch.setDaysInStep(0);
                    logger.debug(String.format("Batch ready for packaging: %s", batch));
                });

        //move completed batches to inventory
        batchService.getByStep(BrewStep.PACKAGE).forEach(batch -> {
            inventoryService.updateQuantity(batch.getRecipe().getName(), batch.getRecipe().getVolume());
            batchService.deleteBatch(batch.getBatchId());
            logger.debug(String.format("Batch completed and moved to inventory: %s", batch));
        });

        //move all completed fermenting batches to carbonation vessels (if available)
        SortedSet<Batch> batchesToCarbonate = batchService.getByStep(BrewStep.FERMENT).stream()
                .filter(b -> b.getDaysInStep() >= b.getRecipe().getFermentationDays())
                .collect(Collectors.toCollection(sortedSetSupplierCarbonation));

        batchesToCarbonate.forEach(batch -> {
            if(batchService.getByStep(BrewStep.CARBONATE).size() < CARBONATION_VESSELS) {
                batch.setCurrentStep(BrewStep.CARBONATE);
                batch.setDaysInStep(0);
                logger.debug(String.format("Batch moved to carbonation vessel: %s", batch));
            } else {
                logger.debug(String.format("No available carbonation vessel for batch: %s", batch));
            }
        });
    }

    @Override
    public Set<DayOfWeek> getDaysToProcess() {
        return Constants.TRANSFER_DAYS;
    }

    @Override
    public String getName() {
        return "TransferProcessor";
    }
}
