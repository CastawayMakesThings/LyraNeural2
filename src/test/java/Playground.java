public class Playground {
    public static void main(String[] args) {
        String originalString = "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "0.0/\n" +
                "1.0/\n" +
                "1.0/\n" +
                "~\n" +
                "0.01392226966344367/\n" +
                "0.003783303402068652/\n" +
                "0.004053228507598569/\n" +
                "0.015750154383056887/\n" +
                "0.014657000619035607/\n" +
                "-3.460456293035066E-4/\n" +
                "-1.3508833684777288E-4/\n" +
                "0.004945178914773765/\n" +
                "0.0016199671678053256/\n" +
                "0.010916391272081456/\n" +
                "-0.003085954797211997/\n" +
                "-0.00448086379634654/\n" +
                "-0.01156142985521908/\n" +
                "0.01204794551404193/\n" +
                "0.005094239237911042/\n" +
                "-0.003311912292059152/\n" +
                "-7.083824121922477E-4/\n" +
                "7.063693555135118E-4/\n" +
                "0.010702855696191737/\n" +
                "-0.008223731978386016/\n" +
                "-0.007063587470392717/\n" +
                "-0.0025176335962027903/\n" +
                "-0.0015700147862467426/\n" +
                "0.01917780851246654/\n" +
                "0.0140003128408336/\n" +
                "-0.011694446008769703/\n" +
                "0.014771094362472356/\n" +
                "2.0240019677859818E-4/\n" +
                "-0.017011504481227006/\n" +
                "0.9176852213780675/\n" +
                "0.01916625371111552/\n" +
                "0.964106157414673/";

        originalString = originalString.replace("\n","");

        String i = originalString.split("~")[0];
        String o = originalString.split("~")[1];

        String[] inputs = i.split("/");
        String[] outputs = o.split("/");

        double[] iNumbers = new double[inputs.length];
        double[] oNumbers = new double[outputs.length];

        for (int j = 0; j < inputs.length; j++) {
            inputs[j] = inputs[j].trim();
            iNumbers[j] = Double.parseDouble(inputs[j]);
        }
        for (int j = 0; j < outputs.length; j++) {
            outputs[j] = outputs[j].trim();
            oNumbers[j] = Double.parseDouble(outputs[j]);
        }

        for (int j = 0; j < oNumbers.length; j++) {
            if(oNumbers[j] > 0.5) {
                System.out.println(iNumbers[j] + "  1");
            } else {
                System.out.println(iNumbers[j] + "  0");
            }
        }
    }
}
