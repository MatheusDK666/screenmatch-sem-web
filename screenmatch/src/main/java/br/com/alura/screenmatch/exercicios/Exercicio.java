package br.com.alura.screenmatch.exercicios;

public class Exercicio {
    public static void main(String[] args) {

        IFuncional multiplicar = (a, b) -> a * b;
        System.out.println(multiplicar.multiplicar(6, 6));

        IFuncionalParouImpar verificarprimo = a -> {
            if ( a <= 1) return false;
            for (int i = 2; i <= Math.sqrt(a); i++){
                if (a % i == 0) return false;
            }
            return true;
        };
        System.out.println(verificarprimo.parOuIMpar(11));
        System.out.println(verificarprimo.parOuIMpar(12));

        ICaixaAlta descricao = String::toUpperCase;
        System.out.println(descricao.caixaAlta("oi tudo bem?"));
    }
}
