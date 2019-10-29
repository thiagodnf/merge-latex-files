package thiagodnf.latex.merge;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import thiagodnf.latex.merge.merger.Merger;

public class Launcher {

    public static Pattern pattern = Pattern.compile("^\\\\(include|input)\\{(.*)\\}$");

    public static void main(String[] args) throws IOException {

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    new Launcher().run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void run() throws IOException {

        FileNameExtensionFilter filter = new FileNameExtensionFilter("LaTeX Files", "tex");

        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setFileFilter(filter);

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File mainFile = chooser.getSelectedFile();

            Path folder = mainFile.toPath().getParent();

            Merger merger = new Merger(mainFile);

            String merged = merger.merge();

            Path target = folder.resolve("merged.tex");

            FileUtils.write(target.toFile(), merged, StandardCharsets.UTF_8);

            JOptionPane.showMessageDialog(null, "Done");
        }
    }
}
