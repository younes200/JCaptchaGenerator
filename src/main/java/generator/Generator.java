package generator;

import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.gimpy.Gimpy;
import com.octo.captcha.image.ImageCaptchaFactory;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import java.util.Arrays;

import com.octo.captcha.engine.image.gimpy.DeformedBaffleListGimpyEngine;

import java.nio.file.Files;
import org.apache.commons.cli.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Description:
 * </p>
 *
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class Generator {

    public static void writeCaptch(final String word, final String outputFile)
            throws FileNotFoundException, IOException {
        final CustomEngine engine = new CustomEngine(word);
        final ImageCaptchaFactory factory = engine.getImageCaptchaFactory();
        final ImageCaptcha pixCaptcha = factory.getImageCaptcha();

        final BufferedImage bi = pixCaptcha.getImageChallenge();

        ImageIO.write(bi, "jpg", new FileOutputStream(Paths.get(outputFile).toFile()));
    }

    public static void generateCombination(String outputFolder) throws IOException {

        List<List<String>> combinations = org.paukov.combinatorics3.Generator.permutation("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
                .withRepetitions(4)
                .stream()
                .collect(Collectors.<List<String>>toList());                        
            
        for(List<String> item : combinations){
            String classe = String.join("", item);
                final Path imageFile = Paths.get(outputFolder, classe+ ".jpg");
                System.out.println(imageFile);
                writeCaptch(classe, imageFile.toString());
        }
    }


    public static void generateCombinationWithVariants(String outputFolder, int repetition) throws IOException {

        List<List<String>> combinations = org.paukov.combinatorics3.Generator.permutation("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
                .withRepetitions(4)
                .stream()
                .collect(Collectors.<List<String>>toList());                        
            
        for(List<String> item : combinations){
            String classe = String.join("", item);
            for (int i=0; i<repetition;i++) {
                final Path classFolder = Paths.get(outputFolder, classe);
                classFolder.toFile().mkdir();
                final Path imageFile = Paths.get(classFolder.toString(), classe + "_v" + i + ".jpg");
                System.out.println(imageFile);
                writeCaptch(classe, imageFile.toString());
            }
        }


    }

    public static void generateList(final String outputFolder, final int count) throws IOException {
        final Path dataFile = Paths.get(outputFolder, "pass.txt");

        final CustomEngine engine = new CustomEngine(null);
        final ImageCaptchaFactory factory = engine.getImageCaptchaFactory();

        final BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile.toString(), true));

        for (int i = 0; i < count; i++) {

            final ImageCaptcha pixCaptcha = factory.getImageCaptcha();

            final Gimpy gimpy = (Gimpy) pixCaptcha;

            final String phrase = gimpy.getResponse();

            // System.out.println(pixCaptcha.validateResponse("1111"));

            final BufferedImage bi = pixCaptcha.getImageChallenge();

            final Path imageFile = Paths.get(outputFolder, i + ".jpg");

            final File f = new File(imageFile.toString());
            ImageIO.write(bi, "jpg", new FileOutputStream(f));

            writer.write(phrase);
            writer.write(' ');

            if ((i) % 100 == 0) {
                System.out.print('.');
            }

        }
        writer.close();
    }

    

    public static void main(final String[] args) throws IOException {

        final Options options = new Options();

        final Option input = new Option("f", "folder", true, "output folder, default to Data/");
        input.setRequired(false);
        options.addOption(input);

        final Option output = new Option("n", "count", true, "count of captcha to generate, default to 20000");
        output.setType(Number.class);
        output.setRequired(false);
        options.addOption(output);

        final Option repetitionOption = new Option("r", "repetition", true, "Combinaition repetition");
        repetitionOption.setType(Number.class);
        repetitionOption.setRequired(false);
        options.addOption(repetitionOption);

        final CommandLineParser parser = new DefaultParser();
        final HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

       
        
        try {
            cmd = parser.parse(options, args);
            final String outputFolder = cmd.getOptionValue("folder", "samples");

            int count = 20000;
            if (cmd.hasOption("count")) {
                count = ((Number) cmd.getParsedOptionValue("count")).intValue();
            }

            int repetition = 10;
            if (cmd.hasOption("repetition")) {
                repetition = ((Number) cmd.getParsedOptionValue("repetition")).intValue();
            }

            File file = new File(outputFolder);
            file.mkdir();

            // generateCombination(outputFolder, repetition);
            generateCombinationWithVariants(outputFolder, repetition);
            // writeCaptch("1111", outputFolder);
            //generateList(outputFolder, count);

        } catch (final ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

    }

}