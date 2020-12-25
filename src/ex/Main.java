package ex;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    // dimension initiale de la fenêtre principale de l'application
    final int height = 400;
    final int width = 700;

    // police de caractère de la zone de saisie
    final String fontName = "verdana";
    final int fontSize = 16;

    /*
    final String example =
        "color 1\n" +
        "forward 20\n" +
        "right 90\n" +
        "forward 20\n" +
        "color 5\n" +
        "left 90\n" +
        "forward 20\n" +
        "right 90\n" +
        "forward 20\n" +
        "color 11\n" +
        "forward 20\n" +
        "color 8\n" +
        "repeat 4 [\n" +
        "  forward 15\n" +
        "  left 90\n" +
        "]\n";
    */

    final static String example =
        "procedure p1 [\n" +
        "    forward 10\n" +
        "    right 90\n" +
        "]\n" +
        "procedure p2 [\n" +
        "    color 11\n" +
        "    right 90\n" +
        "    forward 15\n" +
        "    color 1\n" +
        "    repeat 4 [\n" +
        "        call p1\n" +
        "    ]\n" +
        "    right 180\n" +
        "    color 11\n" +
        "    forward 15\n" +
        "    right 90\n" +
        "]\n" +
        "color 11\n" +
        "left 90\n" +
        "forward 40\n" +
        "right 90\n" +
        "repeat 10 [\n" +
        "    color 3\n" +
        "    forward 40\n" +
        "    right 36\n" +
        "    call p2\n" +
        "]\n";

    BufferedImage image;

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        SwingUtilities.invokeLater(() -> (new Main()).initSwingGui());
    }

    private void initSwingGui() {

        // mainPanel

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 0, 0));

        JTextArea textArea = new JTextArea(example);
        textArea.setFont(new Font(fontName, Font.PLAIN, fontSize));

        JScrollPane scrollTextArea = new JScrollPane(textArea);
        mainPanel.add(scrollTextArea);

        @SuppressWarnings("serial")
		JComponent drawPanel = new JComponent() {
            protected void paintComponent(Graphics g) {
                Dimension d = getSize();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, d.width, d.height);
                if (image != null) g.drawImage(image, 0, 0, null);
            }
        };
        drawPanel.setOpaque(true);
        mainPanel.add(drawPanel);

        // bottomPanel

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        JButton btnRun = new JButton("run");
        btnRun.setFont(new Font(fontName, Font.PLAIN, fontSize));

        bottomPanel.add(btnRun);

        JLabel msgLabel = new JLabel();
        msgLabel.setOpaque(true);
        msgLabel.setFont(new Font(fontName, Font.PLAIN, fontSize));
        GridBagConstraints msgLabelGridBagConstraints = new GridBagConstraints();
        msgLabelGridBagConstraints.fill=GridBagConstraints.BOTH;
        msgLabelGridBagConstraints.weightx=1;
        bottomPanel.add(msgLabel, msgLabelGridBagConstraints);

        // Gestion de l'interpréteur

        btnRun.addActionListener(e -> {
            Dimension d = drawPanel.getSize();
            image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0,0, d.width, d.height);
            g.setColor(Color.BLACK);
            msgLabel.setText("");
            try {
                (new Interpreter()).interpreter(textArea.getText(), d.width / 2, d.height / 2, g);
                drawPanel.repaint();
                msgLabel.setText("ok");
            } catch (Exception ex) {
                msgLabel.setText(ex.toString());
            }
        });

        // Fenêtre principale

        JFrame frame = new JFrame("Démo projet TLA 20-21");
        frame.setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = frame.getContentPane();
        container.add(mainPanel, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.PAGE_END);

        frame.pack();
        frame.setVisible(true);

    }
}
