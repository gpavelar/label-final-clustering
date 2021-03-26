import java.util.TreeSet;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import java.io.FileWriter;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gustavo
 */
public class SelecaoTopicosMelhores {

    public static void main(String[] args) {

        // Try-catch para pegar algumas exceções que podem aparecer durante a execução
        try {

            Configuration conf = new Configuration();
            Job job = new Job(conf, "OpenTopicsSelection");
            job.setJarByClass(SelecaoTopicosMelhores.class);

//            for (int dir = 1; dir <= 10; dir++) {
            /**
             * URI - Caminho para o arquivo topicos.
             *
             */
//            String uriTopicTerm = "/pmreduzida-output/"+ dir +/topicterm-0";
            String uriTopicTerm = "/pmreduzida-output/1/topicterm-0";
            /**
             * URI - Caminho para o arquivo tfidf.
             *
             */
            String uriTfIdf = "/pmreduzida-vectors/tfidf-vectors/part-r-00000";

            System.out.println(uriTopicTerm);
            System.out.println(uriTfIdf);

            Path pathTopicTerm = new Path(uriTopicTerm);

            FileSystem fsTopicTerm = FileSystem.get(URI.create(uriTopicTerm), conf);

            ArrayList<VectorWritable> clusters = new ArrayList<>();

            SequenceFile.Reader readerTopicTerm = null;

            readerTopicTerm = new SequenceFile.Reader(fsTopicTerm, pathTopicTerm, conf);

            String arquivoSaidaArquivos = "/home/gustavo/Documentos";

            FileWriter fStreamTfIdfReduzido;

            // Criação de um FileWriter para gravar os labels originais
            FileWriter fStreamTopicTerm;

//            fStreamTfIdfReduzido = new FileWriter(arquivoSaidaArquivos + "/basetfidf_e" + dir, true);
            fStreamTfIdfReduzido = new FileWriter(arquivoSaidaArquivos + "/basetfidf_e1", true);

            
//            fStreamTopicTerm = new FileWriter(arquivoSaidaArquivos + "/baseTopicTerm_e" + dir , true);
            fStreamTopicTerm = new FileWriter(arquivoSaidaArquivos + "/baseTopicTerm_e1", true);

            // TopicTerm
            // Key class: class org.apache.hadoop.io.IntWritable Value Class: class org.apache.mahout.math.VectorWritable
            IntWritable keyObjTopicTerm = (IntWritable) readerTopicTerm.getKeyClass().newInstance();
            VectorWritable valueObjTopicTerm = (VectorWritable) readerTopicTerm.getValueClass().newInstance();

            List<Double> dadosTopicTerms = new ArrayList<>();

            List<Integer> indexTopicTerms = new ArrayList<>();
            List<Integer> novoIndexTopicTerms = new ArrayList<>();

            System.out.println("\t Etapa 1 - # Criacao do ArrayList para quantidade 'X' de topicos #");
//                ArrayList<ArrayList<ValoresTopicosIndex>>[] objetosTopicos = new ArrayList[5];
            ArrayList<ValoresTopicosIndex>[] objetosTopicos = new ArrayList[5];

            // For de inicialização
            
            objetosTopicos[0] = new ArrayList<>();
            objetosTopicos[1] = new ArrayList<>();
            objetosTopicos[2] = new ArrayList<>();
            objetosTopicos[3] = new ArrayList<>();
            objetosTopicos[4] = new ArrayList<>();

            int identificacaoTopico = 0;
            
            System.out.println("\t Etapa 1 - # Seleçao dos topicos/termos que estao presentes no arquivo LDA #");
            while (readerTopicTerm.next(keyObjTopicTerm, valueObjTopicTerm)) {

                valueObjTopicTerm.get();

                VectorWritable objTopicTerm = valueObjTopicTerm;
                clusters.add(objTopicTerm);

                Vector Centroides = objTopicTerm.get();

                int auxiliar = 0;

                for (int index = 0; index <= (Centroides.size() - 1); index++) {
                    if ((Centroides.getQuick(index) != 0) && (auxiliar < (Centroides.size() - 1))) {
                        fStreamTopicTerm.append(String.valueOf((index) + "\n"));
                        indexTopicTerms.add(index);
                        // Verificar a qual Topico Pertence.
                        objetosTopicos[identificacaoTopico].add(new ValoresTopicosIndex(index, Centroides.get(index)));
                        auxiliar++;
                    } else if ((Centroides.getQuick(index) != 0) && (auxiliar == (Centroides.size() - 1))) {
                        fStreamTopicTerm.append(String.valueOf((index) + "\n"));
                        indexTopicTerms.add(index);
                        objetosTopicos[identificacaoTopico].add(new ValoresTopicosIndex(index, Centroides.get(index)));
                    }
                }
                identificacaoTopico++;
            }

            // Terminou a seleção dos indices.
            List<Integer> novaListaIndex = new ArrayList<>(new TreeSet<>(indexTopicTerms));
            List<Double> novaListaDados = new ArrayList<>(new TreeSet<>(dadosTopicTerms));

//                Collections.sort(objetosTopicos[0], new VTIComparator());
//                Collections.sort(objetosTopicos[2], new ValoresTopicosIndex());
            System.out.println("\t Etapa 1 - # Ordenaçao dos vetores de topicos de forma decrescente de acordo a p(term|topic) #");
            Collections.sort(objetosTopicos[0], new VTIComparator());
            Collections.sort(objetosTopicos[1], new VTIComparator());
            Collections.sort(objetosTopicos[2], new VTIComparator());
            Collections.sort(objetosTopicos[3], new VTIComparator());
            Collections.sort(objetosTopicos[4], new VTIComparator());

            System.out.println(objetosTopicos[0].get(0).getValorPorcentagemPalavra() + " - " + objetosTopicos[0].get(0).getValorIndicePalavra() + " - " + objetosTopicos[0].size());
            System.out.println(objetosTopicos[1].get(0).getValorPorcentagemPalavra() + " - " + objetosTopicos[1].get(0).getValorIndicePalavra() + " - " + objetosTopicos[1].size());
            System.out.println(objetosTopicos[2].get(0).getValorPorcentagemPalavra() + " - " + objetosTopicos[2].get(0).getValorIndicePalavra() + " - " + objetosTopicos[2].size());
            System.out.println(objetosTopicos[3].get(0).getValorPorcentagemPalavra() + " - " + objetosTopicos[3].get(0).getValorIndicePalavra() + " - " + objetosTopicos[3].size());
            System.out.println(objetosTopicos[4].get(0).getValorPorcentagemPalavra() + " - " + objetosTopicos[4].get(0).getValorIndicePalavra() + " - " + objetosTopicos[4].size());

            System.out.println("\t Etapa 2 - # Seleçao de ate 100 termos por topico #");
            /**
             * int qtdAtributos = 0;
             *
             * for(int u = 0; u < 5; u ++){
             *
             * for( int x = 0; x < 160; x++){
             * novoIndexTopicTerms.add(objetosTopicos[u].get(x).getValorIndicePalavra());
             * qtdAtributos++; }
             *
             * qtdAtributos++; }
             */

            int topic1 = 0;
            int topic2 = 0;
            int topic3 = 0;
            int topic4 = 0;
            int topic5 = 0;

            int indice1 = 0;
            int indice2 = 1;
            int indice3 = 2;
            int indice4 = 3;
            int indice5 = 4;

            int flag = 1;
            int qtdAcrescentou = 0;

            List<Integer> resultadoIndices = null;

            for (int t = 0; t < objetosTopicos[0].size(); t++) {
                flag = 1;
                for (int u = 0; u < 5; u++) {
                    for (int j = u + 1; j < 5; j++) {

                        if ((flag == 1) && (objetosTopicos[u].get(t).getValorIndicePalavra() != objetosTopicos[j].get(t).getValorIndicePalavra())) {
//                            if (qtdAcrescentou < 20) {
//                                System.out.println(objetosTopicos[u].get(t).getValorIndicePalavra() + " - " + objetosTopicos[j].get(t).getValorIndicePalavra());
//                            }
                            flag = 1;
                        } else {
                            flag = 0;
                        }

                    }
                    if (flag == 1) {
                        if ((resultadoIndices == null)) {
                            topic1++;
                            topic2++;
                            topic3++;
                            topic4++;
                            topic5++;
                            qtdAcrescentou++;

                            for (int ui = 0; ui < 5; ui++) {
                                novoIndexTopicTerms.add(objetosTopicos[ui].get(t).getValorIndicePalavra());
                            }
                            resultadoIndices = new ArrayList<>(new TreeSet<>(novoIndexTopicTerms));
                            break;
                        } else {

//                                if (((topic1 <= 100) || (topic2 <= 100) || (topic3 <= 100) || (topic4 <= 100) || (topic5 <= 100)) && (resultadoIndices.size() < 500)) {
                            if ((resultadoIndices.size() < 500)) {
                                if (topic1 <= 100) {
                                    novoIndexTopicTerms.add(objetosTopicos[0].get(t).getValorIndicePalavra());
                                    topic1++;
                                }
//                                else if(topic1 > 100 && resultadoIndices.size() < 500) {
//                                    novoIndexTopicTerms.add(objetosTopicos[0].get(t).getValorIndicePalavra());
//                                    
//                                }

                                if (topic2 <= 100) {
                                    novoIndexTopicTerms.add(objetosTopicos[1].get(t).getValorIndicePalavra());
                                    topic2++;
                                }
//                                else if(topic2 > 100 && resultadoIndices.size() < 500) {
//                                    novoIndexTopicTerms.add(objetosTopicos[1].get(t).getValorIndicePalavra());
//                                  
//                                }

                                if (topic3 <= 100) {
                                    novoIndexTopicTerms.add(objetosTopicos[2].get(t).getValorIndicePalavra());
                                    topic3++;
                                }
//                                else if(topic3 > 100 && resultadoIndices.size() < 500) {
//                                    novoIndexTopicTerms.add(objetosTopicos[2].get(t).getValorIndicePalavra());
//                                    
//                                }

                                if (topic4 <= 100) {
                                    novoIndexTopicTerms.add(objetosTopicos[3].get(t).getValorIndicePalavra());
                                    topic4++;
                                }
//                                else if(topic4 > 100 && resultadoIndices.size() < 500) {
//                                    novoIndexTopicTerms.add(objetosTopicos[3].get(t).getValorIndicePalavra());
//                                    
//                                }
//                                
                                if (topic5 <= 100) {
                                    novoIndexTopicTerms.add(objetosTopicos[4].get(t).getValorIndicePalavra());
                                    topic5++;
                                }
//                                else if(topic5 > 100 && resultadoIndices.size() < 500) {
//                                    novoIndexTopicTerms.add(objetosTopicos[4].get(t).getValorIndicePalavra());
//                                    
//                                }
//                                

                                qtdAcrescentou++;

//                                for (int ui = 0; ui < 5; ui++) {
//                                    novoIndexTopicTerms.add(objetosTopicos[ui].get(t).getValorIndicePalavra());
//                                }
                                resultadoIndices = new ArrayList<>(new TreeSet<>(novoIndexTopicTerms));
                            }
                            break;

                        }
                    } else {
//                        System.out.print(" :D ");
                    }
                }
            }

//                List<ValoresTopicosIndex> novoObjetosTopicos = new ArrayList<>(new TreeSet<>(objetosTopicos[0]));
            System.out.println(novaListaIndex.size());
            System.out.println(novaListaDados.size());

            System.out.println("Valores indices \n");
            System.out.println(topic1 + " - " + topic2 + " - " + topic3 + " - " + topic4 + " - " + topic5);

            resultadoIndices = new ArrayList<>(new TreeSet<>(novoIndexTopicTerms));
            System.out.println(resultadoIndices.size());
//                System.out.println(novoObjetosTopicos.toString());

//            for (Integer value : novaLista) {
//                System.out.println(value);
//            }
            // TFIDF
            Path pathTfIdf = new Path(uriTfIdf);

            FileSystem fsTfIdf = FileSystem.get(URI.create(uriTfIdf), conf);

            SequenceFile.Reader readerTfIdf = null;

            readerTfIdf = new SequenceFile.Reader(fsTfIdf, pathTfIdf, conf);

            Writable keyObjTfIdf = (Writable) readerTfIdf.getKeyClass().newInstance();

            VectorWritable valueObjTfIdf = (VectorWritable) readerTfIdf.getValueClass().newInstance();

            // Verificar se o documento não esta todo 0. 
            System.out.println("\t Etapa 3 - # Verificacao de quantos possuem todos valores iguais a 0 #");

            int indiceDocumento = 0;
            List<Integer> documentosRemovidos = new ArrayList<>();
            Double somaTfIdf;

            while (readerTfIdf.next(keyObjTfIdf, valueObjTfIdf)) {

                somaTfIdf = 0.0;
//                List<java.util.Vector> vetorDadosTfIdf = new ArrayList<>();
                org.apache.mahout.math.NamedVector vectorDadosTfIdf = (org.apache.mahout.math.NamedVector) valueObjTfIdf.get();
//                Vector vetorTfIdf = valueObjTfIdf.get();
                int aux = 0;
                for (Integer value : resultadoIndices) {
                    somaTfIdf = somaTfIdf + vectorDadosTfIdf.getQuick(value);
                }

                if (somaTfIdf == 0) {
                    // adiciona o indice do documento ao vetor de indices de documentos
                    // documento não tem que entrar na lista de saida
                    documentosRemovidos.add(indiceDocumento);

                } else {
                    // Entra na lista de documentos que serão trabalhados
                }
                indiceDocumento++;
//                fStreamTfIdfReduzido.append(String.valueOf("\n"));
            }

            System.out.println(indiceDocumento + " - Tamanho do indice de documentos removidos - " + documentosRemovidos.size());
            for (Integer value : documentosRemovidos) {
                System.out.println("Valores removidos :" + value);
            }

            readerTfIdf = new SequenceFile.Reader(fsTfIdf, pathTfIdf, conf);

            System.out.println("\t Etapa 4 - # Gerando arquivo de saída tfidf de acordo com os termos selecionados #");
            // Começou a obtenção dos valores nos indices presentes
            int contadorDocumento = 0;

            int flagDocExcluido;
            while (readerTfIdf.next(keyObjTfIdf, valueObjTfIdf)) {
                flagDocExcluido = 0;

//                List<java.util.Vector> vetorDadosTfIdf = new ArrayList<>();
                org.apache.mahout.math.NamedVector vectorDadosTfIdf = (org.apache.mahout.math.NamedVector) valueObjTfIdf.get();
//                Vector vetorTfIdf = valueObjTfIdf.get();
                int aux = 0;
                for (Integer valoresDocumentosExcluidos : documentosRemovidos) {
                    if (valoresDocumentosExcluidos == contadorDocumento) {
                        flagDocExcluido = 1;
                    } else {
                        flagDocExcluido = 0;
                    }

                }

                if (flagDocExcluido != 1) {

                    for (Integer value : resultadoIndices) {

                        if (aux < (resultadoIndices.size() - 1)) {

                            //                    fStreamTfIdfReduzido.append(String.valueOf(vetorTfIdf.getQuick(value) + " "));
                            fStreamTfIdfReduzido.append(String.valueOf(vectorDadosTfIdf.getQuick(value) + " "));
                            aux++;
                        } else {
                            fStreamTfIdfReduzido.append(String.valueOf(vectorDadosTfIdf.getQuick(value)));

                        }
                    }
                    fStreamTfIdfReduzido.append(String.valueOf("\n"));
                    contadorDocumento++;
                }

            }

//                for (int index = 0; index <= (vectorDadosTfIdf.size() - 1); index++) {
//                    if (auxiliar < (vectorDadosTfIdf.size() - 1)) {
////                                    fStreamValue.append(String.valueOf(vectorDados.get(index) + " "));
//                        fStreamTfIdfReduzido.append(String.valueOf(vectorDadosTfIdf.getQuick(index) + " "));
//                        auxiliar++;
//                    } else {
//                        fStreamTfIdfReduzido.append(String.valueOf((index) + "\n"));
//                    }
//
//            for (Integer value : novaLista) {
//                System.out.println(value);
//            }
            fStreamTfIdfReduzido.close();
            fStreamTopicTerm.close();
//            }
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(SelecaoTopicosMelhores.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SelecaoTopicosMelhores.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

class VTIComparator implements Comparator<ValoresTopicosIndex> {

    @Override
    public int compare(ValoresTopicosIndex o1, ValoresTopicosIndex o2) {
        ValoresTopicosIndex var1 = (ValoresTopicosIndex) o1;
        ValoresTopicosIndex var2 = (ValoresTopicosIndex) o2;

        if (var1.getValorPorcentagemPalavra() > var2.getValorPorcentagemPalavra()) {
            return -1;
        } else if (var1.getValorPorcentagemPalavra() < var2.getValorPorcentagemPalavra()) {
            return 1;
        } else {
            return 0;
        }
    }

}

class VTIComparatorIndice implements Comparator<ValoresTopicosIndex> {

    @Override
    public int compare(ValoresTopicosIndex o1, ValoresTopicosIndex o2) {
        ValoresTopicosIndex var1 = (ValoresTopicosIndex) o1;
        ValoresTopicosIndex var2 = (ValoresTopicosIndex) o2;

        if (var1.getValorIndicePalavra() > var2.getValorIndicePalavra()) {
            return 1;
        } else if (var1.getValorIndicePalavra() < var2.getValorIndicePalavra()) {
            return -1;
        } else {
            return 0;
        }
    }

}
