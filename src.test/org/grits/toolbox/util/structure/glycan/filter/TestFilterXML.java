package org.grits.toolbox.util.structure.glycan.filter;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.sugar.SubstituentType;
import org.grits.toolbox.core.dataShare.PropertyHandler;
import org.grits.toolbox.util.structure.glycan.filter.om.Category;
import org.grits.toolbox.util.structure.glycan.filter.om.ComboFilter;
import org.grits.toolbox.util.structure.glycan.filter.om.Filter;
import org.grits.toolbox.util.structure.glycan.filter.om.FilterOrder;
import org.grits.toolbox.util.structure.glycan.filter.om.FilterSequence;
import org.grits.toolbox.util.structure.glycan.filter.om.FiltersLibrary;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterAnd;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterLinkage;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterMonosaccharide;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterSubstituent;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterSubstructure;
import org.grits.toolbox.util.structure.glycan.filter.om.MonosaccharideDefintion;
import org.grits.toolbox.util.structure.glycan.util.FilterUtils;

public class TestFilterXML
{
    public static void main(String[] args)
    {
        FiltersLibrary filters = new FiltersLibrary();
        List<Category> categories = new ArrayList<>();
        Category cat1 = new Category();
        cat1.setLabel("N-Glycan");
        cat1.setName("N-Glycan");
        cat1.setDescription("Filters applicable to N-Glycans");
        List<String> filterNames = new ArrayList<>();
        filterNames.add("Hex");
        filterNames.add("KDN");
        cat1.setFilters(filterNames);
        categories.add(cat1);
        filters.setCategories(categories);
        
        filters.setName("MS Filters");
        GlycanFilterMonosaccharide filter1 = new GlycanFilterMonosaccharide();
        filter1.setLabel("Hex");
        filter1.setName("Hex");
        filter1.setDescription("Hexose residues");
        filter1.setMonosaccharide(
                TestFilterXML.toMonosaccharideDefintion("RES\n" + "1b:x-HEX-1:5"));
        filters.addFilter(filter1);

        GlycanFilterMonosaccharide filter2 = new GlycanFilterMonosaccharide();
        filter2.setLabel("KDN");
        filter2.setName("KDN");
        filter2.setDescription("KDN residues");
        filter2.setMonosaccharide(TestFilterXML
                .toMonosaccharideDefintion("RES\n" + "1b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d"));
        filters.addFilter(filter2);

        GlycanFilterMonosaccharide filter3 = new GlycanFilterMonosaccharide();
        filter3.setLabel("Neu");
        filter3.setName("Neu");
        filter3.setDescription("Neu residues");
        filter3.setMonosaccharide(TestFilterXML
                .toMonosaccharideDefintion("RES\n" + "1b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n"
                        + "2s:amino\n" + "LIN\n" + "1:1d(5+1)2n"));
        filters.addFilter(filter3);

        GlycanFilterMonosaccharide filter4 = new GlycanFilterMonosaccharide();
        filter4.setLabel("NeuAc");
        filter4.setName("NeuAc");
        filter4.setDescription("NeuAc residues");
        filter4.setMonosaccharide(TestFilterXML
                .toMonosaccharideDefintion("RES\n" + "1b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n"
                        + "2s:n-acetyl\n" + "LIN\n" + "1:1d(5+1)2n"));
        filters.addFilter(filter4);

        GlycanFilterMonosaccharide filter5 = new GlycanFilterMonosaccharide();
        filter5.setLabel("NeuGc");
        filter5.setName("NeuGc");
        filter5.setDescription("NeuGc residues");
        filter5.setMonosaccharide(TestFilterXML
                .toMonosaccharideDefintion("RES\n" + "1b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n"
                        + "2s:n-glycolyl\n" + "LIN\n" + "1:1d(5+1)2n"));
        filters.addFilter(filter5);

        GlycanFilterMonosaccharide filter6 = new GlycanFilterMonosaccharide();
        filter6.setLabel("HexNAc");
        filter6.setName("HexNAc");
        filter6.setDescription("HexNAc residues");
        filter6.setMonosaccharide(TestFilterXML.toMonosaccharideDefintion(
                "RES\n" + "1b:x-HEX-1:5\n" + "2s:n-acetyl\n" + "LIN\n" + "1:1d(2+1)2n"));
        filters.addFilter(filter6);

        GlycanFilterSubstituent filter7 = new GlycanFilterSubstituent();
        filter7.setSubstituent(SubstituentType.SULFATE.getName());
        filter7.setDescription("Sulphate modification");
        filter7.setLabel("S");
        filter7.setName("Sulphate");
        filters.addFilter(filter7);

        GlycanFilterSubstituent filter8 = new GlycanFilterSubstituent();
        filter8.setSubstituent(SubstituentType.PHOSPHATE.getName());
        filter8.setDescription("Phosphate modification");
        filter8.setLabel("P");
        filter8.setName("Phosphate");
        filters.addFilter(filter8);

        GlycanFilterMonosaccharide filter9 = new GlycanFilterMonosaccharide();
        filter9.setLabel("dHex");
        filter9.setName("dHex");
        filter9.setDescription("deoxy-Hexose residues");
        filter9.setMonosaccharide(
                TestFilterXML.toMonosaccharideDefintion("RES\n" + "1b:x-HEX-1:5|6:d"));
        filters.addFilter(filter9);

        GlycanFilterMonosaccharide filter10 = new GlycanFilterMonosaccharide();
        filter10.setLabel("Pen");
        filter10.setName("Pen");
        filter10.setDescription("Pentose residues");
        filter10.setMonosaccharide(
                TestFilterXML.toMonosaccharideDefintion("RES\n" + "1b:x-PEN-x:x"));
        filters.addFilter(filter10);

        GlycanFilterMonosaccharide filter11 = new GlycanFilterMonosaccharide();
        filter11.setLabel("HexA");
        filter11.setName("HexA");
        filter11.setDescription("Hexuronic acid residues");
        filter11.setMonosaccharide(
                TestFilterXML.toMonosaccharideDefintion("RES\n" + "1b:x-HEX-1:5|6:a"));
        filters.addFilter(filter11);

        GlycanFilterSubstructure filter12 = new GlycanFilterSubstructure();
        filter12.setDescription("Gal(1-3)Gal Epitope");
        filter12.setLabel("GalGal");
        filter12.setName("GalGal");
        List<FilterSequence> sequences = new ArrayList<>();
        FilterSequence sequence = new FilterSequence();
        sequence.setSequence(
                "RES\n" + "1b:x-dgal-HEX-1:5\n" + "2b:a-dgal-HEX-1:5\n" + "LIN\n" + "1:1o(3+1)2d");
        sequences.add(sequence);
        filter12.setSubstructure(sequences);
        filters.addFilter(filter12);

        GlycanFilterSubstructure filter13 = new GlycanFilterSubstructure();
        filter13.setDescription("Bisected N-Glycans");
        filter13.setLabel("BiSected");
        filter13.setName("Bisection");
        sequences = new ArrayList<>();
        sequence = new FilterSequence();
        sequence.setSequence(
                "RES\n" + "1b:x-dglc-HEX-1:5\n" + "2s:n-acetyl\n" + "3b:b-dglc-HEX-1:5\n"
                        + "4s:n-acetyl\n" + "5b:b-dman-HEX-1:5\n" + "6b:a-dman-HEX-1:5\n"
                        + "7b:b-dglc-HEX-1:5\n" + "8s:n-acetyl\n" + "9b:a-dman-HEX-1:5\n" + "LIN\n"
                        + "1:1d(2+1)2n\n" + "2:1o(4+1)3d\n" + "3:3d(2+1)4n\n" + "4:3o(4+1)5d\n"
                        + "5:5o(3+1)6d\n" + "6:5o(4+1)7d\n" + "7:7d(2+1)8n\n" + "8:5o(6+1)9d");
        sequences.add(sequence);
        filter13.setSubstructure(sequences);
        filters.addFilter(filter13);

        List<Filter> filterList = new ArrayList<>();
        filterList.add(filter12);
        filterList.add(filter13);
        ComboFilter combinationFilter = new ComboFilter();
        combinationFilter.setDescription("Structural Type filter");
        combinationFilter.setLabel("Glycan Type");
        combinationFilter.setName("Glycan Type");
        combinationFilter.setFilters(filterList);
        combinationFilter.setSelected(filter13);

        ComboFilter combinationFilter2 = new ComboFilter();
        combinationFilter2.setDescription("Probe Identifier filter");
        combinationFilter2.setLabel("Probe Name");
        combinationFilter2.setName("featureName");

        filters.addFilter(combinationFilter);
        filters.addFilter(combinationFilter2);

        GlycanFilterMonosaccharide filter14 = new GlycanFilterMonosaccharide();
        filter14.setLabel("GlcNAc (Terminal)");
        filter14.setName("GlcNAc (Terminal)");
        filter14.setDescription("glcNac residues");
        filter14.setMonosaccharide(TestFilterXML.toMonosaccharideDefintion(
                "RES\n" + "1b:x-HEX-1:5\n" + "2s:n-acetyl\n" + "LIN\n" + "1:1d(2+1)2n"));
        filter14.setTerminalOnly(true);
        filters.addFilter(filter14);
        
        // test linkage combination filter
        ComboFilter sialylLinkage = new ComboFilter();
        sialylLinkage.setLabel("Sia linkage");
        sialylLinkage.setDescription("Sialic acid linkage filter");
        sialylLinkage.setName("Sialic acid linkage");
     
        filterList = new ArrayList<>();
        List<FilterOrder> filterOrderList = new ArrayList<>();
        
        GlycanFilterLinkage linkage1 = new GlycanFilterLinkage();
        linkage1.setName("Sialic acid: 3");
        linkage1.setLabel("Sialic acid: 3");
        linkage1.setDescription("");
        // chosen pattern
        String t_pattern = "Sialic acid: 3";
        linkage1.setChosenLinkagePattern(t_pattern);
        // monosaccharide
        MonosaccharideDefintion t_defintionMs = new MonosaccharideDefintion();
        t_defintionMs.setAllowModifications(true);
        t_defintionMs.setAllowSubstituents(true);
        t_defintionMs.setSequence("RES\n1b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d");
        linkage1.setMonosaccharide(t_defintionMs);
        // terminal
        linkage1.setTerminal(false);
        
        GlycanFilterLinkage linkage2 = new GlycanFilterLinkage();
        linkage2.setName("Sialic acid: 6");
        linkage2.setLabel("Sialic acid: 6");
        linkage2.setDescription("");
        // chosen pattern
        t_pattern = "Sialic acid: 6";
        linkage2.setChosenLinkagePattern(t_pattern);
        
        linkage2.setMonosaccharide(t_defintionMs);
        linkage2.setTerminal(false);
        
        GlycanFilterLinkage linkage3 = new GlycanFilterLinkage();
        linkage3.setName("Sialic acid: 3,6");
        linkage3.setLabel("Sialic acid: 3,6");
        linkage3.setDescription("");
        // chosen pattern
        t_pattern = "Sialic acid: 3,6";
        linkage3.setChosenLinkagePattern(t_pattern);
        
        linkage3.setMonosaccharide(t_defintionMs);
        linkage3.setTerminal(false);
        
        
        filterList.add(linkage1);
        filterList.add(linkage2);
        filterList.add(linkage3);
        
        FilterOrder order1 = new FilterOrder();
        order1.setFilterName(linkage1.getName());
        order1.setOrder(0);
        
        filterOrderList.add(order1);
        
        
        order1 = new FilterOrder();
        order1.setFilterName(linkage2.getName());
        order1.setOrder(1);
        
        filterOrderList.add(order1);
        
        order1 = new FilterOrder();
        order1.setFilterName(linkage3.getName());
        order1.setOrder(2);
        
        filterOrderList.add(order1);
        
        
        sialylLinkage.setFilters(filterList);
        sialylLinkage.setFilterOrders(filterOrderList);
        sialylLinkage.setSelected(linkage2);
        
        filters.addFilter(sialylLinkage);
        
        GlycanFilterAnd andFilter = new GlycanFilterAnd();
        andFilter.setLabel("My And Filter");
        andFilter.setName("Test AND");
        andFilter.getElements().add(linkage3);
        andFilter.getElements().add(filter14);
        
        filters.addFilter(andFilter);

        try
        {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            List<Class> contextList = new ArrayList<Class>(
                    Arrays.asList(FilterUtils.filterClassContext));
            contextList.add(FiltersLibrary.class);
           // contextList.add(Category.class);
            JAXBContext context = JAXBContext
                    .newInstance(contextList.toArray(new Class[contextList.size()]));
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING,
                    PropertyHandler.GRITS_CHARACTER_ENCODING);
            marshaller.marshal(filters, os);

            FileWriter fileWriter = new FileWriter("filters.xml");
            fileWriter
                    .write(os.toString((String) marshaller.getProperty(Marshaller.JAXB_ENCODING)));
            fileWriter.close();
            os.close();

            // then try to read the filters back
            FiltersLibrary library = FilterUtils.readFilters("filters.xml");
            for (Category c : library.getCategories()) {
            	System.out.println(c);
            	System.out.println("Has filters size: " + c.getFilters().size());
            }
            for (Filter filter : library.getFilters())
            {
                if (filter instanceof GlycanFilterMonosaccharide)
                {
                    System.out.println("Filter " + filter.getName());
                    ((GlycanFilterMonosaccharide) filter).getMonosaccharide().getSequence();
                    System.out.println(
                            "Terminal:" + ((GlycanFilterMonosaccharide) filter).getTerminalOnly());
                }
                else if (filter instanceof GlycanFilterSubstituent)
                    System.out.println(((GlycanFilterSubstituent) filter).getSubstituent());
                else if (filter instanceof GlycanFilterSubstructure)
                {
                    System.out.println("substructure Filter " + filter.getName());
                    for (FilterSequence sequence1 : ((GlycanFilterSubstructure) filter)
                            .getSubstructure())
                    {
                        sequence1.getSubStructure();
                    }
                    ;
                }
                else if (filter instanceof ComboFilter)
                {
                    if (((ComboFilter) filter).getFilters() != null)
                        for (Filter f : ((ComboFilter) filter).getFilters())
                        {
                            System.out.println(f.toString());
                        }
                    System.out.println("Selected:");
                    if (((ComboFilter) filter).getSelected() != null)
                        System.out.println(((ComboFilter) filter).getSelected().toString());
                    if (((ComboFilter) filter).getFilterOrders() != null) 
                    	for (FilterOrder order: ((ComboFilter) filter).getFilterOrders()) {
                    		System.out.print("Filter " + order.getFilterName());
                    		System.out.println("order: " + order.getOrder());
                    	}
                    System.out.println("Filters in filter order!");
                    if (((ComboFilter) filter).getFiltersInFilterOrder() != null)
                        for (Filter f : ((ComboFilter) filter).getFiltersInFilterOrder())
                        {
                            System.out.println(f.toString());
                        }
                }
            }
        }
        catch (IOException | JAXBException e)
        {
            e.printStackTrace();
        }
        catch (SugarImporterException e)
        {
            e.printStackTrace();
        }

    }

    private static MonosaccharideDefintion toMonosaccharideDefintion(String a_sequence)
    {
        MonosaccharideDefintion t_ms = new MonosaccharideDefintion();
        t_ms.setSequence(a_sequence);
        return t_ms;
    }
}
