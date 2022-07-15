package org.grits.toolbox.util.structure.glycan.filter.visitor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.GlycoCT.SugarImporterGlycoCTCondensed;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterOperator;
import org.grits.toolbox.util.structure.glycan.filter.XMLUtil;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterAntenna;
import org.grits.toolbox.util.structure.glycan.filter.om.MonosaccharideDefintion;

public class TestGlycoVisitorCountAntenna
{
    private static Sugar getSugar() throws SugarImporterException
    {
        // Man3 - core fuc + NeuAc + NeuGc + Neu9a + bi-section= 5 Antenna
        String t_sequence = "RES\n1b:b-dglc-HEX-1:5\n2s:n-acetyl\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dman-HEX-1:5\n6b:a-dman-HEX-1:5\n7b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n8s:n-acetyl\n9b:b-dglc-HEX-1:5\n10s:n-acetyl\n11b:a-dman-HEX-1:5\n12b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n13s:amino\n14b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n15s:n-glycolyl\n16b:a-lgal-HEX-1:5|6:d\nLIN\n1:1d(2+1)2n\n2:1o(4+1)3d\n3:3d(2+1)4n\n4:3o(4+1)5d\n5:5o(3+1)6d\n6:6o(4+2)7d\n7:7d(5+1)8n\n8:5o(4+1)9d\n9:9d(2+1)10n\n10:5o(6+1)11d\n11:11o(2+2)12d\n12:12d(5+1)13n\n13:11o(4+2)14d\n14:14d(5+1)15n\n15:1o(6+1)16d\n\n";
        SugarImporterGlycoCTCondensed t_importer = new SugarImporterGlycoCTCondensed();
        return t_importer.parse(t_sequence);
    }

    public static void main(String[] args) throws JAXBException
    {
        TestGlycoVisitorCountAntenna.testXML();
        // test for terminal ms
        TestGlycoVisitorCountAntenna.countAntenna();
    }

    private static void testXML() throws JAXBException
    {
        GlycanFilterAntenna t_filter = new GlycanFilterAntenna();
        t_filter.setName("Name");
        t_filter.setLabel("Label");
        t_filter.setDescription("Description");
        t_filter.setMin(0);
        t_filter.setMax(5);
        t_filter.setFilterBisection(true);
        List<MonosaccharideDefintion> t_ms = new ArrayList<MonosaccharideDefintion>();
        MonosaccharideDefintion t_defintionMs = new MonosaccharideDefintion();
        t_defintionMs.setAllowModifications(false);
        t_defintionMs.setAllowSubstituents(false);
        t_defintionMs.setSequence("RES\n1b:x-lgal-HEX-1:5|6:d");
        t_ms.add(t_defintionMs);
        t_defintionMs = new MonosaccharideDefintion();
        t_defintionMs.setAllowModifications(false);
        t_defintionMs.setAllowSubstituents(false);
        t_defintionMs.setSequence("RES\n1b:x-dxyl-PEN-1:5");
        t_ms.add(t_defintionMs);
        t_filter.setExcludeMonosaccharide(t_ms);
        XMLUtil.testXML(t_filter);
    }

    private static void countAntenna()
    {
        try
        {
            // build the sugar
            Sugar t_sugar = TestGlycoVisitorCountAntenna.getSugar();
            // build the filter
            GlycanFilterAntenna t_filter = new GlycanFilterAntenna();
            // run test
            System.out.println("Search without restriction: "
                    + GlycanFilterOperator.applyAntennaFilter(t_sugar, t_filter));
            MonosaccharideDefintion t_defintionMs = new MonosaccharideDefintion();
            t_defintionMs.setAllowModifications(true);
            t_defintionMs.setAllowSubstituents(false);
            t_defintionMs.setSequence("RES\n1b:a-lgal-HEX-1:5");
            t_filter.getExcludeMonosaccharide().add(t_defintionMs);
            // run test
            System.out.println(
                    "Search no Fuc: " + GlycanFilterOperator.applyAntennaFilter(t_sugar, t_filter));
            t_defintionMs = new MonosaccharideDefintion();
            t_defintionMs.setAllowModifications(true);
            t_defintionMs.setAllowSubstituents(true);
            t_defintionMs.setSequence("RES\n1b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d");
            t_filter.getExcludeMonosaccharide().add(t_defintionMs);
            // run test
            System.out.println("Search no Fuc + Sia: "
                    + GlycanFilterOperator.applyAntennaFilter(t_sugar, t_filter));
            t_filter.setFilterBisection(false);
            // run test
            System.out.println("Search no Fuc + Sia (allow bisection): "
                    + GlycanFilterOperator.applyAntennaFilter(t_sugar, t_filter));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
