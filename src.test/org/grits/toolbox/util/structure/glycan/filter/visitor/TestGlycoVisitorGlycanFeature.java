package org.grits.toolbox.util.structure.glycan.filter.visitor;

import javax.xml.bind.JAXBException;

import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.GlycoCT.SugarImporterGlycoCTCondensed;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterOperator;
import org.grits.toolbox.util.structure.glycan.filter.XMLUtil;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterGlycanFeature;

public class TestGlycoVisitorGlycanFeature
{

    private static Sugar getSugar() throws SugarImporterException
    {
        // Man3 - core fuc + NeuAc + NeuGc + Neu
        String t_sequence = "RES\n1b:x-dglc-HEX-1:5\n2s:n-acetyl\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dman-HEX-1:5\n6b:a-dman-HEX-1:5\n7b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n8s:n-acetyl\n9b:a-dman-HEX-1:5\n10b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n11s:amino\n12b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n13s:n-glycolyl\n14b:a-lgal-HEX-1:5|6:d\nLIN\n1:1d(2+1)2n\n2:1o(4+1)3d\n3:3d(2+1)4n\n4:3o(4+1)5d\n5:5o(3+1)6d\n6:6o(4+2)7d\n7:7d(5+1)8n\n8:5o(6+1)9d\n9:9o(2+2)10d\n10:10d(5+1)11n\n11:9o(4+2)12d\n12:12d(5+1)13n\n13:1o(-1+1)14d";
        SugarImporterGlycoCTCondensed t_importer = new SugarImporterGlycoCTCondensed();
        return t_importer.parse(t_sequence);
    }

    public static void main(String[] args) throws JAXBException
    {
        TestGlycoVisitorGlycanFeature.testXML();
        TestGlycoVisitorGlycanFeature.testGlcNAc();
    }

    private static void testXML() throws JAXBException
    {
        GlycanFilterGlycanFeature t_filter = new GlycanFilterGlycanFeature();
        t_filter.setName("Name");
        t_filter.setLabel("Label");
        t_filter.setDescription("Description");
        XMLUtil.testXML(t_filter);
    }

    public static void testGlcNAc()
    {
        try
        {
            // build the sugar
            Sugar t_sugar = TestGlycoVisitorGlycanFeature.getSugar();
            // build the filter
            GlycanFilterGlycanFeature t_filter = new GlycanFilterGlycanFeature();
            System.out.println("No restriction: "
                    + GlycanFilterOperator.applyGlycanFeatureFilter(t_sugar, t_filter));
            t_filter.setUnknownAnomerAllowed(false);
            // run test
            System.out.println("No anomer: "
                    + GlycanFilterOperator.applyGlycanFeatureFilter(t_sugar, t_filter));
            t_filter.setUnknownAnomerAllowed(true);
            t_filter.setUnknownLinkagePositionAllowed(false);
            // run test
            System.out.println("No unknown linkage: "
                    + GlycanFilterOperator.applyGlycanFeatureFilter(t_sugar, t_filter));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
