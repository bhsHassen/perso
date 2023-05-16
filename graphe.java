import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

public class ArticleGraph {
    public static void main(String[] args) {
        String csvFile = "chemin_vers_votre_fichier.csv";
        String line;
        String csvSplitBy = ",";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> dates = new ArrayList<>();
        List<Integer> entrees = new ArrayList<>();
        List<Integer> sorties = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                Date date = dateFormat.parse(data[0]);
                int nombreEntrees = Integer.parseInt(data[1]);
                int nombreSorties = Integer.parseInt(data[2]);
                dates.add(date);
                entrees.add(nombreEntrees);
                sorties.add(nombreSorties);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < dates.size(); i++) {
            dataset.addValue(entrees.get(i), "Articles en entrée", dateFormat.format(dates.get(i)));
            dataset.addValue(sorties.get(i), "Articles en sortie", dateFormat.format(dates.get(i)));
        }

        JFreeChart chart = ChartFactory.createLineChart(
            "Nombre d'articles en entrée et en sortie",
            "Date",
            "Nombre d'articles",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        // Personnaliser les couleurs des séries
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.getRenderer().setSeriesPaint(0, Color.BLUE);
        plot.getRenderer().setSeriesPaint(1, Color.RED);

        // Sauvegarder le graphique en PDF
        try (FileOutputStream file = new FileOutputStream("chemin_vers_votre_fichier.pdf")) {
            Document document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();
            ChartUtilities.writeChartAsPDF(document, chart, 500, 400);
            document.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
