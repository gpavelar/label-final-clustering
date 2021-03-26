/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labelfinalclusterpp;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author gustavo
 */


import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.mahout.math.AbstractVector;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.MultiLabelVectorWritable;

import java.io.FileWriter;
import java.util.Iterator;

import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.classify.WeightedPropertyVectorWritable;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.math.RandomAccessSparseVector;

/**
 *
 * @author gustavo
 */
public class LabelFinalClusterpp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Try-catch para pegar algumas exceções que podem aparecer durante a execução
        try {
            
            Configuration conf = new Configuration();
            Job job = new Job(conf, "OpenFinalCluster");
            job.setJarByClass(LabelFinalClusterpp.class);

            /**
             * URI - Caminho para o arquivo final após o clusterpp sobre o diretório /clusters-*-final.
             **/
            //System.out.println("/test1/opkmeans/clusters-2-final/part-r-00000");
            //String uri = "/baseStopStem/tfidf-vectors/part-r-00000";
           
//            String uri = "/dbssvd/saida1/resposta/167/part-m-0";
            
//            String uri = "/clusterppdiff-pca/ex10/1171/part-m-0";
//            String uri = "/clusterppdiff-pca/ex10/2834/part-m-0";
//            String uri = "/clusterppdiff-pca/ex10/3422/part-m-0";
//            String uri = "/clusterppdiff-pca/ex10/4446/part-m-0";
            String uri = "/clusterppdiff-pca/ex10/4582/part-m-0";
            
            /*** URICENTROIDES - Colocar o caminho dos centroides ou o caminho do diretoriofinal(clusterpp).
             */
            //String uriCentroides = "/test1/opkmeans/clusters-2-final/part-r-00000";
//            String uriCentroides = "/clusterppdiff-pca/ex10/1171/part-m-0";;
//            String uriCentroides = "/clusterppdiff-pca/ex10/2834/part-m-0";
//            String uriCentroides = "/clusterppdiff-pca/ex10/3422/part-m-0";
//            String uriCentroides = "/clusterppdiff-pca/ex10/4446/part-m-0";
            String uriCentroides = "/clusterppdiff-pca/ex10/4582/part-m-0";
            
            int labelCluster = 5;
            // Key class: class org.apache.hadoop.io.LongWritable Value Class: class org.apache.mahout.math.VectorWritable
            
            System.out.println(uriCentroides);

            Path pathCentroides = new Path(uriCentroides);
            
            FileSystem fsCentroides = FileSystem.get(URI.create(uriCentroides), conf);
            // Seta caminho do arquivo sequencial a ser aberto.
       
            ArrayList<ClusterWritable> clusters = new ArrayList<ClusterWritable>();
            
            SequenceFile.Reader readerCentroides = null;
            
            readerCentroides = new SequenceFile.Reader(fsCentroides, pathCentroides, conf);
            String arquivoSaidaArquivos = "/home/gustavo/Experimentos_1";
            
            FileWriter fStreamValueCentroides;
            fStreamValueCentroides = new FileWriter(arquivoSaidaArquivos + "/centroides", true);

            LongWritable keyCentroides = (LongWritable) readerCentroides.getKeyClass().newInstance();
//            IntWritable keyCentroides = (IntWritable) readerCentroides.getKeyClass().newInstance();
            VectorWritable valueCentroides = (VectorWritable) readerCentroides.getValueClass().newInstance();
            
            fStreamValueCentroides.close();
            
            // Caminho Principal !!!
            FileSystem fs = FileSystem.get(URI.create(uri), conf);
            // Seta caminho do arquivo sequencial a ser aberto.

            Path path = new Path(uri);

            SequenceFile.Reader reader = null;
            try {
                //Key class: class org.apache.hadoop.io.LongWritable Value Class: class org.apache.mahout.math.VectorWritable
                // Leitor de arquivo sequencial recebe o arquivo do caminho informado
                reader = new SequenceFile.Reader(fs, path, conf);
                // Leitura da key
                Writable key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
                // Leitura do Value
                Writable value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
                // Leitura da posição do arquivo
                long position = reader.getPosition();
//                 Criação de um FileWriter para gravar as keys
                FileWriter fStream;
                // Criação de um FileWriter para gravar as Values
                FileWriter fStreamValue;
                // Criação de um FileWriter para gravar os labels originais
                FileWriter fStreamLabels;
//                
//                
//
                while (reader.next(key, value)) {
                    String syncSeen = reader.syncSeen() ? "*" : "";
//                    //escreve arquivo contendo o tempo de execução do programa
//                    
//                    
                    try {
//                        /**
//                         * * Execução para PCA ***
//                         */
//                      //fStream = new FileWriter(arquivoSaidaLabels + "/ValorChaves", true);
                        fStreamValue = new FileWriter(arquivoSaidaArquivos + "/arquivo", true);
                        fStreamLabels = new FileWriter(arquivoSaidaArquivos + "/labelsenomes", true);
                 
                        // CONVERSÃO DO VALUE PARA WRITABLE, PARA OBTER O VETOR                     
                        Writable writableValue = value;
                        
                        
        
                        // CONVERSÃO APRA VETORWRITABLE, PARA OBTER OS INDICES SEPARADOS
                        VectorWritable intWritable = (VectorWritable) writableValue;  // cast
                       
                        org.apache.mahout.math.NamedVector vect = (org.apache.mahout.math.NamedVector) intWritable.get();
                        
//                      // VERIFICAÇÃO PARA ARQUIVO de entrada alterar em todos, caso use.
                        //if (String.valueOf(key).contains("/CMC")) { 
                        // Verificação para segundo arquivo
                        if(String.valueOf(intWritable).contains("/alt")){
//                        if(String.valueOf(intWritable).contains("/CMC")){
                            // GERA OS LABELS
                            
                            fStreamLabels.append(String.valueOf(vect.getName()) + "\t1" + "\t"+labelCluster);
                            //fStreamLabels.append("1");
                            fStreamLabels.append("\n");
                            fStreamLabels.flush();
 
                        } else if (String.valueOf(intWritable).contains("/msf")) {
//                        } else if (String.valueOf(intWritable).contains("/GN")) {
//                            // GERA OS LABELS
                            fStreamLabels.append(String.valueOf(vect.getName()) + "\t2"+ "\t"+labelCluster);
                            //fStreamLabels.append("2");
                            fStreamLabels.append("\n");
                            fStreamLabels.flush();
 
                        } else if (String.valueOf(intWritable).contains("/rsb")) {
//                         } else if (String.valueOf(intWritable).contains("/DS")) {
                        // GERA OS LABELS
                        fStreamLabels.append(String.valueOf(vect.getName()) + "\t3"+ "\t"+labelCluster);
//                            fStreamLabels.append("3");
                            fStreamLabels.append("\n");
                            fStreamLabels.flush();
                            
                        } else if (String.valueOf(intWritable).contains("/sci")) {
//                            } else if (String.valueOf(intWritable).contains("/MT")) {
                        // GERA OS LABELS
                        fStreamLabels.append(String.valueOf(vect.getName()) + "\t4"+ "\t"+labelCluster);
//                            fStreamLabels.append("4");
                            fStreamLabels.append("\n");
                            fStreamLabels.flush();
                            
                        } else if (String.valueOf(intWritable).contains("/tpm")) {
//                            } else if (String.valueOf(intWritable).contains("/SP")) {
                        // GERA OS LABELS
                        fStreamLabels.append(String.valueOf(vect.getName()) + "\t5" + "\t"+labelCluster);
//                            fStreamLabels.append("5");
                            fStreamLabels.append("\n");
                            fStreamLabels.flush();
                         
                        }
                        
                    fStreamValue.close();
                    fStreamLabels.close();
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }

                }
        } catch (IOException ex) {
            Logger.getLogger(LabelFinalClusterpp.class.getName()).log(Level.SEVERE, null, ex);
        }
    } catch (IOException ex2){
        Logger.getLogger(LabelFinalClusterpp.class.getName()).log(Level.SEVERE, null, ex2);
    }   catch (InstantiationException ex) {
            Logger.getLogger(LabelFinalClusterpp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(LabelFinalClusterpp.class.getName()).log(Level.SEVERE, null, ex);
        }
}

}