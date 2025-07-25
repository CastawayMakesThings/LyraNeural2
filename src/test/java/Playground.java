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
                "-0.008901017508640016/\n" +
                "0.003295752519179543/\n" +
                "-0.0014923924345146351/\n" +
                "-0.007095745471200216/\n" +
                "0.0028342276397364204/\n" +
                "-0.004756483326423558/\n" +
                "-0.004467705653458965/\n" +
                "-0.0037632695105404663/\n" +
                "0.010394962088702244/\n" +
                "-0.01242307110326382/\n" +
                "4.971653373512186E-4/\n" +
                "0.0016177850895711756/\n" +
                "-0.005885627036045712/\n" +
                "0.0018603246120406525/\n" +
                "0.012538987787395615/\n" +
                "-0.01413075695877618/\n" +
                "0.00260815585670002/\n" +
                "0.014309709620323439/\n" +
                "-0.0017994990943047608/\n" +
                "0.0014654718072897792/\n" +
                "0.007305552423425651/\n" +
                "-0.005763928598191265/\n" +
                "-0.0151470019655909/\n" +
                "0.003048417517164469/\n" +
                "-0.00694202758543392/\n" +
                "-1.9286002943244606E-4/\n" +
                "-0.015203349661906403/\n" +
                "0.007448011781192209/\n" +
                "-0.009301713327679935/\n" +
                "0.9247578121956003/\n" +
                "0.013680847483887721/\n" +
                "0.9664338132944571/";

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
