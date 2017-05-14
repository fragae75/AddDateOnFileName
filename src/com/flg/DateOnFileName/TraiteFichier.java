package com.flg.DateOnFileName;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JTextArea;

public class TraiteFichier {

    //
    // Méthode   : LastModified + traitement avec Calendar
    //
	public void renameFilesByModificationDate(boolean bSimulation, String sDir, JTextArea jtLog){
	    File file = new File(sDir);
        String sFileName = new String();
        String sNewFileName = new String();
        long lDate;
    	Calendar c = Calendar.getInstance();
		
		try {
		      //On parcourt la liste des fichiers et répertoires
		      for(File fCurrentFile : file.listFiles()){
		        //S'il s'agit d'un dossier, on ajoute un "/"
		        if (fCurrentFile.isDirectory())
		        {
			    	  System.out.println(fCurrentFile.getName()+"\\");
			    	  jtLog.setCaretPosition(jtLog.getDocument().getLength());
			    	  jtLog.append(fCurrentFile.getName()+"\\" + "\n");
			    	  renameFilesByModificationDate(bSimulation, sDir + "\\" + fCurrentFile.getName()+"\\", jtLog);
		        }
		        else
		        {
			        sFileName = fCurrentFile.getName();
			        if (sFileName.toLowerCase().contains(".mp3") && !sFileName.toLowerCase().contains("!-!")){
	                	lDate = fCurrentFile.lastModified();
			        	
			        	c.setTimeInMillis(lDate);
			        	sNewFileName = String.format("%02d %02d %02d !-! %s", c.get(Calendar.YEAR)-2000, c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), sFileName);
			        	System.out.println(fCurrentFile.getName() + " => " + sDir + sNewFileName);
			        	File fFileToRename = new File(sDir + sNewFileName);
			    		jtLog.setCaretPosition(jtLog.getDocument().getLength());
			    		jtLog.append(fCurrentFile.getName() + " => " + sDir + sNewFileName + "\n");
			        	if (!bSimulation)
			        		fCurrentFile.renameTo(fFileToRename);
//			        	fCurrentFile.getAtt
			        }
			        else
			        {
				        System.out.println(fCurrentFile.getName() + " => no change \n");
			    		jtLog.setCaretPosition(jtLog.getDocument().getLength());
			    		jtLog.append(fCurrentFile.getName() + " => no change \n");
			        }
		        }
		      } // for
		
		      System.out.println("\n");
	    	  jtLog.setCaretPosition(jtLog.getDocument().getLength());
	    	  jtLog.append("\n");
	    	  
		   } catch (NullPointerException e) {
               System.out.println("Exception handled when trying to get file " +
                       "attributes: " + e.getMessage());	                
		   }
	} // renameFilesByModificationDate

}

/* 
 * 
 * 
 * tests préliminaires
 * 
 * 
 */

/*
    //
    // Méthode   : Creation Date + traitement avec Calendar
    // !!! pas maintenu
	public void renameFilesByCreationDate(String sDir){
	    File file = new File(sDir);
        String sFileName = new String();
        String sNewFileName = new String();
        long lDate;
    	Calendar c = Calendar.getInstance();
        
		
		try {
		      int i = 1;  
		      //On parcourt la liste des fichiers et répertoires
		      for(File nom : file.listFiles()){
		        //S'il s'agit d'un dossier, on ajoute un "/"
		        if (nom.isDirectory())
		        {
//			    	  System.out.println("\t\t" + ((nom.isDirectory()) ? nom.getName()+"/" : nom.getName()));
			    	  System.out.println(nom.getName()+"\\");
			    	  renameFilesByCreationDate(sDir + "\\" + nom.getName()+"\\");
		        }
		        sFileName = nom.getName();
		        System.out.println(nom.getName()+"\\");
		        if (sFileName.toLowerCase().contains(".mp3")){

	                //
	                // Méthode 1  : date de création avec BasicFileAttributes + traitement avec Calendar
	                //
		            BasicFileAttributes attributes = null;
		            Path filePath = nom.toPath();
	                try
	                {
			            attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
			            lDate = attributes.creationTime().to(TimeUnit.MILLISECONDS);
			            
			            if((lDate > Long.MIN_VALUE) && (lDate < Long.MAX_VALUE))
			            {
				        	c.setTimeInMillis(lDate);
				        	sNewFileName = String.format("1) %02d %02d %02d - %s", c.get(Calendar.YEAR)-2000, c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), sFileName);
				        	System.out.println(sNewFileName);
			            }	                
			        }
	                catch (IOException exception)
	                {
	                    System.out.println("Exception handled when trying to get file " +
	                            "attributes: " + exception.getMessage());	                
	                }
		        }
		        	
		    	
		      }
		
		      System.out.println("\n");
		   } catch (NullPointerException e) {
		        //L'instruction peut générer une NullPointerException
		        //s'il n'y a pas de sous-fichier !
		   }
	}
	
	
	
	public void parcoursRepertoiresx (String sDir){
	    File file = new File(sDir);
        String sFileName = new String();
        String sNewFileName = new String();
        long lDate;
    	Calendar c = Calendar.getInstance();
        
		
		try {
		      int i = 1;  
		      //On parcourt la liste des fichiers et répertoires
		      for(File nom : file.listFiles()){
		        //S'il s'agit d'un dossier, on ajoute un "/"
		        if (nom.isDirectory())
		        {
//			    	  System.out.println("\t\t" + ((nom.isDirectory()) ? nom.getName()+"/" : nom.getName()));
			    	  System.out.println(nom.getName()+"\\");
			          parcoursRepertoiresx(sDir + "\\" + nom.getName()+"\\");
		        }
		        sFileName = nom.getName();
		        System.out.println(nom.getName()+"\\");
		        if (sFileName.toLowerCase().contains(".mp3")){

	                //
	                // Méthode 1  : date de création avec BasicFileAttributes + traitement avec Calendar
	                //
		            BasicFileAttributes attributes = null;
		            Path filePath = nom.toPath();
	                try
	                {
			            attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
			            lDate = attributes.creationTime().to(TimeUnit.MILLISECONDS);
			            if((lDate > Long.MIN_VALUE) && (lDate < Long.MAX_VALUE))
			            {
				        	c.setTimeInMillis(lDate);
				        	sNewFileName = String.format("1) %02d %02d %02d - %s", c.get(Calendar.YEAR)-2000, c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), sFileName);
				        	System.out.println(sNewFileName);
			            }	                
			        }
	                catch (IOException exception)
	                {
	                    System.out.println("Exception handled when trying to get file " +
	                            "attributes: " + exception.getMessage());	                
	                }
	                
	                //
	                // Méthode 2  : LastModified + traitement avec Calendar
	                //
                	lDate = nom.lastModified();
		        	
		        	c.setTimeInMillis(lDate);
		        	sNewFileName = String.format("2) %02d %02d %02d - %s", c.get(Calendar.YEAR)-2000, c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), sFileName);
		        	System.out.println(sNewFileName);
		        	//		        	nom.renameTo(sFileName);
		        	
		        }
		        	
		    	
		      }
		
		      System.out.println("\n");
		   } catch (NullPointerException e) {
		        //L'instruction peut générer une NullPointerException
		        //s'il n'y a pas de sous-fichier !
		   }
	}
	
	public void TestParcoursRepertoires() {
	    File f = new File("test.txt");
	    System.out.println("Chemin absolu du fichier : " + f.getAbsolutePath());
	    System.out.println("Nom du fichier : " + f.getName());
	    System.out.println("Est-ce qu'il existe ? " + f.exists());
	    System.out.println("Est-ce un répertoire ? " + f.isDirectory());
	    System.out.println("Est-ce un fichier ? " + f.isFile());
	    System.out.println("Affichage des lecteurs à la racine du PC : ");
	
	   
	    for(File file : f.listRoots())
	    {
	      System.out.println(file.getAbsolutePath());
	
	      try {
	        int i = 1;  
	        //On parcourt la liste des fichiers et répertoires
	        for(File nom : file.listFiles()){
	          //S'il s'agit d'un dossier, on ajoute un "/"
	          System.out.print("\t\t" + ((nom.isDirectory()) ? nom.getName()+"/" : nom.getName()));
	          if((i%4) == 0){
	            System.out.print("\n");
	          }
	          i++;
	        }
	
	        System.out.println("\n");
	      } catch (NullPointerException e) {
	        //L'instruction peut générer une NullPointerException
	        //s'il n'y a pas de sous-fichier !
	      }
	
	    }       	
	 }
}
*/