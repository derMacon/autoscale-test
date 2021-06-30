package dps.hoffmann.proxy.model;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static dps.hoffmann.proxy.model.LogicalService.NODE;
import static dps.hoffmann.proxy.model.ScalingInstructionUtils.*;

class StatsGeneratorTest {

    @Test
    public void testUpdateSpecificGaugeRefsLowerTier() {
        ScalingInstruction instr1 = createInstr(NODE, "fstBatchId", 1);
        ScalingInstruction instr2 = createInstr(NODE, "fstBatchId", 3);

        List<ScalingInstruction> lst = Arrays.asList(new ScalingInstruction[]{instr1, instr2});

        StatsGenerator gen = new StatsGenerator(NODE, lst);
        AtomicInteger[] arr = genRandomArr(30);
        gen.updateSpecificGaugeRefs(arr);

        Assert.isTrue(arr[0].get() == 0);
        Assert.isTrue(arr[1].get() == 2000);
        Assert.isTrue(arr[2].get() == 0);
    }

    @Test
    public void testUpdateSpecificGaugeRefsMultipleBatches() {
        ScalingInstruction instr1 = createInstr(NODE, "fstBatchId", 2);
        ScalingInstruction instr2 = createInstr(NODE, "fstBatchId", 2);
        ScalingInstruction instr3 = createInstr(NODE, "fstBatchId", 4);
        ScalingInstruction instr4 = createInstr(NODE, "fstBatchId", 4);

        ScalingInstruction instr5 = createInstr(NODE, "sndBatchId", 1);
        ScalingInstruction instr6 = createInstr(NODE, "sndBatchId", 1);

        List<ScalingInstruction> input = Arrays.asList(new ScalingInstruction[]{
                instr1,
                instr2,
                instr3,
                instr4,
                instr5,
                instr6
        });


        StatsGenerator gen = new StatsGenerator(NODE, input);
        AtomicInteger[] arr = genRandomArr(30);
        gen.updateSpecificGaugeRefs(arr);

        Assert.isTrue(arr[0].get() == 0);
        Assert.isTrue(arr[1].get() == 1000);
        Assert.isTrue(arr[2].get() == 0);
        Assert.isTrue(arr[3].get() == 3000);
    }

    @Test
    public void testUpdateTierGaugeRefsLowerTier() {
        ScalingInstruction instr1 = createInstr(NODE, "fstBatchId", 6);
        ScalingInstruction instr2 = createInstr(NODE, "fstBatchId", 6);
        ScalingInstruction instr3 = createInstr(NODE, "fstBatchId", 4);
        ScalingInstruction instr4 = createInstr(NODE, "fstBatchId", 4);
        ScalingInstruction instr5 = createInstr(NODE, "fstBatchId", 5);

        ScalingInstruction instr6 = createInstr(NODE, "sndBatchId", 2);
        ScalingInstruction instr7 = createInstr(NODE, "sndBatchId", 2);

        ScalingInstruction instr8 = createInstr(NODE, "middleTierBatch", 2);
        ScalingInstruction instr9 = createInstr(NODE, "middleTierBatch", 2);
        ScalingInstruction instr10 = createInstr(NODE, "middleTierBatch", 6);
        ScalingInstruction instr11 = createInstr(NODE, "middleTierBatch", 6);
        ScalingInstruction instr12 = createInstr(NODE, "middleTierBatch", 4);
        ScalingInstruction instr13 = createInstr(NODE, "middleTierBatch", 4);

        ScalingInstruction instr14 = createInstr(NODE, "thrdBatchId", 6);
        ScalingInstruction instr15 = createInstr(NODE, "thrdBatchId", 6);
        ScalingInstruction instr16 = createInstr(NODE, "thrdBatchId", 4);
        ScalingInstruction instr17 = createInstr(NODE, "thrdBatchId", 4);
        ScalingInstruction instr18 = createInstr(NODE, "thrdBatchId", 5);

        List<ScalingInstruction> input = Arrays.asList(new ScalingInstruction[]{
                instr1,
                instr2,
                instr3,
                instr4,
                instr5,
                instr6,
                instr7,
                instr8,
                instr9,
                instr10,
                instr11,
                instr12,
                instr13,
                instr14,
                instr15,
                instr16,
                instr17,
                instr18
        });


        StatsGenerator gen = new StatsGenerator(NODE, input);
        AtomicInteger[] arr = genRandomArr(3);
        gen.updateTierGaugeRefs(arr, createScalePropsIt());

        Assert.isTrue(arr[0].get() == 4000);
        Assert.isTrue(arr[1].get() == 4000);
        Assert.isTrue(arr[2].get() == 0);

    }

}