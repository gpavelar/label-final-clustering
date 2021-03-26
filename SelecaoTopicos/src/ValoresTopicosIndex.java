
import java.util.Comparator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gustavo
 */
public class ValoresTopicosIndex implements Comparator<ValoresTopicosIndex>{
    
    
    private int valorIndicePalavra;
    private double valorPorcentagemPalavra;
    
    public ValoresTopicosIndex(){
    
    }
    
    public ValoresTopicosIndex(int vp, double valorp){
        this.setValorIndicePalavra(vp);
        this.setValorPorcentagemPalavra(valorp);
    }

    public int getValorIndicePalavra() {
        return valorIndicePalavra;
    }

    public void setValorIndicePalavra(int valorIndicePalavra) {
        this.valorIndicePalavra = valorIndicePalavra;
    }

    public double getValorPorcentagemPalavra() {
        return valorPorcentagemPalavra;
    }

    public void setValorPorcentagemPalavra(double valorPorcentagemPalavra) {
        this.valorPorcentagemPalavra = valorPorcentagemPalavra;
    }

//    @Override
//    public int compare(ValoresTopicosIndex o1, ValoresTopicosIndex o2) {
//        ValoresTopicosIndex var1 = (ValoresTopicosIndex) o1;
//        ValoresTopicosIndex var2 = (ValoresTopicosIndex) o2;
//        
//        if(var1.getValorPorcentagemPalavra() > var2.getValorPorcentagemPalavra()){
//            return -1; 
//        } else if(var1.getValorPorcentagemPalavra() < var2.getValorPorcentagemPalavra()){
//            return 1;
//        } else {
//            return 0;
//        }
//    }
    
    @Override
    public int compare(ValoresTopicosIndex o1, ValoresTopicosIndex o2) {
        ValoresTopicosIndex var1 = (ValoresTopicosIndex) o1;
        ValoresTopicosIndex var2 = (ValoresTopicosIndex) o2;
        
        if(var1.getValorPorcentagemPalavra() > var2.getValorPorcentagemPalavra()){
            return -1; 
        } else if(var1.getValorPorcentagemPalavra() < var2.getValorPorcentagemPalavra()){
            return 1;
        } else {
            return 0;
        }
    }


}