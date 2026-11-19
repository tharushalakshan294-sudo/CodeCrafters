package com.codecrafters.babyshop.util;
import com.codecrafters.babyshop.model.Txn; import java.io.*; import java.nio.file.*; import java.util.List;
public class CsvExporter {
 public void exportTransactions(List<Txn> rows,Path file)throws IOException{try(BufferedWriter w=Files.newBufferedWriter(file)){w.write("ID,Date,Description,Type,Amount");w.newLine();for(Txn t:rows){w.write(t.getId()+","+csv(t.getDate())+","+csv(t.getDesc())+","+csv(t.getType())+","+t.getAmount());w.newLine();}}}
 private String csv(String s){return "\""+(s==null?"":s.replace("\"","\"\""))+"\"";}
}
