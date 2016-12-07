package com.bytebeats.lucene.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * @author Ricky Feng
 * @version V2.0.0
 *
 */
public final class FileUtils {

	public static void read(String fileName, String charset,
			BufferedReadProc rProc) throws IOException {

		read(new FileInputStream(fileName), charset, rProc);
	}

	public static void read(File file, String charset,
			BufferedReadProc rProc) throws IOException {

		read(new FileInputStream(file), charset, rProc);
	}
	
	public static void read(InputStream in, String charset,
			BufferedReadProc rProc) throws IOException {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in, charset));
			
			rProc.read(br);

		}finally {
			IOUtils.closeQuietly(br);
		}
	}
	
	
	public static void write(String fileName, String charset, boolean append, BufferedWriteProc wProc) throws IOException {
		
		write(new File(fileName), charset, append, wProc);
	}
	
	public static void write(File file, String charset, boolean append, BufferedWriteProc wProc) throws IOException {

		BufferedWriter bw = null;
		try {

			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file,append), charset));

			wProc.write(bw);

		}finally {
			IOUtils.closeQuietly(bw);
		}
	}
	
	public static void write(OutputStream out, String charset, BufferedWriteProc wProc) throws IOException {

		BufferedWriter bw = null;
		try {

			bw = new BufferedWriter(new OutputStreamWriter(
					out, charset));

			wProc.write(bw);

		}finally {
			IOUtils.closeQuietly(bw);
		}
	}
	
	public static byte[] read(File file) throws IOException {
		
		InputStream in = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			in = new FileInputStream(file);
			
			IOUtils.copy(in, baos);
			
			return baos.toByteArray();
			
		}finally{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(baos);
		}
	}
	
	public static void read(File file, InputStreamReadProc proc) throws IOException {
		
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			proc.read(in);
			
		}finally{
			IOUtils.closeQuietly(in);
		}
	}
	
	public static void write(File file, OutputStreamWriteProc proc) throws IOException {
		
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			proc.write(out);
			
		}finally{
			IOUtils.closeQuietly(out);
		}
	}
	
	public static void cp(File srcFile, File destFile) throws IOException {
		
		if(srcFile==null || !srcFile.exists()){
			return;
		}
		if (!srcFile.canRead() || !srcFile.canWrite()) {
			return;
		}
		
		if (srcFile.isFile()) {	//single file
			InputStream in = null;
			OutputStream out = null;
			try {
				createFile(destFile);//create
				in = new FileInputStream(srcFile); 
				out = new FileOutputStream(destFile);
				IOUtils.copy(in, out);
			}finally{
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		}else if(srcFile.isDirectory()){	// directory

			if(!destFile.exists()){
				destFile.mkdirs();
			}
			File[] files = srcFile.listFiles();
			if(files!=null){
				for (File file : files) {
					cp(file, new File(destFile,file.getName()));
				}
			}
		}
	}
	
	public static boolean mv(File srcFile, File destFile) throws IOException {
		delete(destFile);
		return srcFile.renameTo(destFile);
	}

	public static void delete(File target) throws IOException {
		
		if(target==null || !target.exists()){
			return;
		}
		
		if (target.isFile()) {	
			target.delete();
		} else if (target.isDirectory()) {	
			File[] files = target.listFiles();
			if(files!=null){
				for (File file : files) {
					delete(file);
				}
			}
			target.delete();
		}
	}
	
	/**
	 * 新建文件
	 * @param target
	 */
	public static void createFile(File target) throws IOException{
		File parentFile = target.getParentFile();
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		target.createNewFile();
	}
	
	/**
	 * 获取该目录的子文件
	 * @param root
	 * @return
	 */
	public static List<File> listFiles(File root) {
		
		if(root==null || !root.exists()){
			return null;
		}
		
		if(root.isDirectory()){
			File[] files = root.listFiles();
			if(files!=null){
				List<File> allFileList = new ArrayList<File>();
				for (File file : files) {
					if(file.isFile()){
						allFileList.add(file);
					}
				}
				return allFileList;
			}
		}
		return null;
	}
	
	/**
	 * 通过递归得到某一路径下所有的文件
	 */
	public static void listFiles(File root, List<File> file_list) {
		
		if(root==null || !root.exists()){
			return;
		}
		
		File[] files = root.listFiles();
		if(files!=null){
			for (File file : files) {
				if (file.isDirectory()) {
					listFiles(file, file_list);
				} else {
					file_list.add(file);
				}
			}
		}
		
	}
	
	public interface InputStreamReadProc {
		
		public void read(InputStream in) throws IOException;
	}
	
	public interface OutputStreamWriteProc {
		
		public void write(OutputStream out) throws IOException;
	}
	
	public interface BufferedWriteProc {
		
		public void write(BufferedWriter writer) throws IOException;
	}

	public interface BufferedReadProc {

		public void read(BufferedReader reader)  throws IOException;
	}
}
