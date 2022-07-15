package org.grits.toolbox.util.structure.glycan.filter.visitor;

import javax.xml.bind.JAXBException;

import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.GlycoCT.SugarImporterGlycoCTCondensed;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterOperator;
import org.grits.toolbox.util.structure.glycan.filter.XMLUtil;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterMonosaccharide;
import org.grits.toolbox.util.structure.glycan.filter.om.MonosaccharideDefintion;

public class TestGlycoVisitorCountMonosaccharide
{
    private static Sugar getSugar() throws SugarImporterException
    {
        // Man5 - bisected
        String t_sequence = "RES\n1b:b-dglc-HEX-1:5\n2s:n-acetyl\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dman-HEX-1:5\n6b:a-dman-HEX-1:5\n7b:b-dglc-HEX-1:5\n8s:n-acetyl\n9b:a-dman-HEX-1:5\n10b:a-dman-HEX-1:5\n11b:a-dman-HEX-1:5\nLIN\n1:1d(2+1)2n\n2:1o(4+1)3d\n3:3d(2+1)4n\n4:3o(4+1)5d\n5:5o(3+1)6d\n6:5o(4+1)7d\n7:7d(2+1)8n\n8:5o(6+1)9d\n9:9o(3+1)10d\n10:9o(6+1)11d";
        SugarImporterGlycoCTCondensed t_importer = new SugarImporterGlycoCTCondensed();
        return t_importer.parse(t_sequence);
    }

    private static Sugar getSugar2() throws SugarImporterException
    {
        // Man3 - core fuc + NeuAc + NeuGc + Neu
        String t_sequence = "RES\n1b:b-dglc-HEX-1:5\n2s:n-acetyl\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dman-HEX-1:5\n6b:a-dman-HEX-1:5\n7b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n8s:n-acetyl\n9b:a-dman-HEX-1:5\n10b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n11s:amino\n12b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n13s:n-glycolyl\n14b:a-lgal-HEX-1:5|6:d\nLIN\n1:1d(2+1)2n\n2:1o(4+1)3d\n3:3d(2+1)4n\n4:3o(4+1)5d\n5:5o(3+1)6d\n6:6o(4+2)7d\n7:7d(5+1)8n\n8:5o(6+1)9d\n9:9o(2+2)10d\n10:10d(5+1)11n\n11:9o(4+2)12d\n12:12d(5+1)13n\n13:1o(6+1)14d";
        SugarImporterGlycoCTCondensed t_importer = new SugarImporterGlycoCTCondensed();
        return t_importer.parse(t_sequence);
    }

    public static void main(String[] args) throws JAXBException
    {
        TestGlycoVisitorCountMonosaccharide.testXML();
        // test for terminal ms
        TestGlycoVisitorCountMonosaccharide.testMan();
        TestGlycoVisitorCountMonosaccharide.testGlcNAc();
        // test for allowed Modifications
        TestGlycoVisitorCountMonosaccharide.testModifications();
        // test for allowed Modifications
        TestGlycoVisitorCountMonosaccharide.testSubstituents();
    }

    private static void testXML() throws JAXBException
    {
        GlycanFilterMonosaccharide t_filter = new GlycanFilterMonosaccharide();
        t_filter.setName("Name");
        t_filter.setLabel("Label");
        t_filter.setDescription("Description");
        t_filter.setMin(0);
        t_filter.setMax(5);
        t_filter.setTerminalOnly(true);
        MonosaccharideDefintion t_defintion = new MonosaccharideDefintion();
        t_defintion.setSequence("RES\n1b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d");
        t_defintion.setAllowModifications(false);
        t_defintion.setAllowSubstituents(false);
        t_filter.setMonosaccharide(t_defintion);
        XMLUtil.testXML(t_filter);
    }

    private static void testSubstituents()
    {
        try
        {
            // build the sugar
            Sugar t_sugar = TestGlycoVisitorCountMonosaccharide.getSugar2();
            // build the filter
            GlycanFilterMonosaccharide t_filter = new GlycanFilterMonosaccharide();
            MonosaccharideDefintion t_defintion = new MonosaccharideDefintion();
            t_defintion.setSequence("RES\n1b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d");
            t_defintion.setAllowModifications(false);
            t_defintion.setAllowSubstituents(false);
            t_filter.setMonosaccharide(t_defintion);
            // run test
            System.out.println("Search for Neu (no other substituents):"
                    + GlycanFilterOperator.applyMonosaccharideFilter(t_sugar, t_filter));
            t_defintion.setAllowSubstituents(true);
            // run test
            System.out.println("Search for Neu (with other substituents): "
                    + GlycanFilterOperator.applyMonosaccharideFilter(t_sugar, t_filter));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void testModifications()
    {
        try
        {
            // build the sugar
            Sugar t_sugar = TestGlycoVisitorCountMonosaccharide.getSugar2();
            // build the filter
            GlycanFilterMonosaccharide t_filter = new GlycanFilterMonosaccharide();
            MonosaccharideDefintion t_defintion = new MonosaccharideDefintion();
            t_defintion.setSequence("RES\n1b:a-lgal-HEX-1:5");
            t_defintion.setAllowModifications(false);
            t_defintion.setAllowSubstituents(false);
            t_filter.setMonosaccharide(t_defintion);
            // run test
            System.out.println("Search for Lgal (no other modification):"
                    + GlycanFilterOperator.applyMonosaccharideFilter(t_sugar, t_filter));
            t_defintion.setAllowModifications(true);
            // run test
            System.out.println("Search for Lgal (with other modification): "
                    + GlycanFilterOperator.applyMonosaccharideFilter(t_sugar, t_filter));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void testMan()
    {
        try
        {
            // build the sugar
            Sugar t_sugar = TestGlycoVisitorCountMonosaccharide.getSugar();
            // build the filter
            GlycanFilterMonosaccharide t_filter = new GlycanFilterMonosaccharide();
            MonosaccharideDefintion t_defintion = new MonosaccharideDefintion();
            t_defintion.setSequence("RES\n1b:x-dman-HEX-1:5");
            t_defintion.setAllowModifications(false);
            t_defintion.setAllowSubstituents(false);
            t_filter.setMonosaccharide(t_defintion);
            // run test
            System.out.println("Mannose (terminal:null): "
                    + GlycanFilterOperator.applyMonosaccharideFilter(t_sugar, t_filter));
            // build the filter
            t_filter.setTerminalOnly(true);
            // run test
            System.out.println("Mannose (terminal:true): "
                    + GlycanFilterOperator.applyMonosaccharideFilter(t_sugar, t_filter));
            t_filter.setTerminalOnly(false);
            // run test
            System.out.println("Mannose (terminal:false): "
                    + GlycanFilterOperator.applyMonosaccharideFilter(t_sugar, t_filter));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void testGlcNAc()
    {
        try
        {
            // build the sugar
            Sugar t_sugar = TestGlycoVisitorCountMonosaccharide.getSugar();
            // build the filter
            GlycanFilterMonosaccharide t_filter = new GlycanFilterMonosaccharide();
            MonosaccharideDefintion t_defintion = new MonosaccharideDefintion();
            t_defintion.setSequence("RES\n1b:x-dglc-HEX-1:5\n2s:n-acetyl\nLIN\n1:1d(2+1)2n");
            t_defintion.setAllowModifications(false);
            t_defintion.setAllowSubstituents(false);
            t_filter.setMonosaccharide(t_defintion);
            // run test
            System.out.println("GlcNAc (terminal:null): "
                    + GlycanFilterOperator.applyMonosaccharideFilter(t_sugar, t_filter));
            // build the filter
            t_filter.setTerminalOnly(true);
            // run test
            System.out.println("GlcNAc (terminal:true): "
                    + GlycanFilterOperator.applyMonosaccharideFilter(t_sugar, t_filter));
            t_filter.setTerminalOnly(false);
            // run test
            System.out.println("GlcNAc (terminal:false): "
                    + GlycanFilterOperator.applyMonosaccharideFilter(t_sugar, t_filter));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
