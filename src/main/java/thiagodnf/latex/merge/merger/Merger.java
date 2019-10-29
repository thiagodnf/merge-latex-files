package thiagodnf.latex.merge.merger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Merger {

    public Pattern pattern = Pattern.compile("^\\\\(include|input)\\{(.*)\\}$");

    public File mainFile;

    public Merger(File mainFile) {
        this.mainFile = mainFile;
    }

    public String merge() throws IOException {
        return merge(mainFile);
    }

    public String merge(File file) throws IOException {

        Path folder = file.toPath().getParent();

        StringBuilder builder = new StringBuilder();

        List<String> lines = Files.readAllLines(file.toPath());

        for (String line : lines) {

            Matcher matcher = pattern.matcher(line);

            if (line.startsWith("% !TeX root")) {
                // Ignore the TexStudio commands
                continue;
            }

            if (matcher.matches()) {

                String filename = matcher.group(2);

                Path p = folder.resolve(filename + ".tex");

                builder.append(merge(p.toFile())).append("\n");

            } else {
                builder.append(line).append("\n");
            }
        }

        return builder.toString();
    }
}
