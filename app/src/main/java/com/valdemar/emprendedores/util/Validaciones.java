package com.valdemar.emprendedores.util;

import android.widget.EditText;

public class Validaciones {

    public static boolean validarEditText(EditText edt) {
        return (edt != null && !edt.getText().toString().isEmpty()) ? true : false;
    }

    public static boolean validarDocumento(String tipo, String numero) {
        numero = numero.replaceAll(" ","");
        switch (tipo) {
            case "DNI":
                return numero.length() == 8;
            case "RUC":
                return numero.length() == 11 && (numero.startsWith("10") || numero.startsWith("20"));
        }
        return false;
    }

    public static boolean validarTelefono(String tipo, String numero){
        numero = numero.replaceAll(" ","");
        switch (tipo) {
            case "TEL":
                return numero.length() == 9 && numero.startsWith("0");
            case "CEL":
                return numero.length() == 9 && numero.startsWith("9");
        }
        return false;
    }
}
