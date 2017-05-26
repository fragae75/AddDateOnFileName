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

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class TraiteFichier {

    //
    // Méthode   : LastModified + traitement avec Calendar
    //
	public void renameFilesByModificationDate(boolean bSimulation, String sDir, JTextArea jtLog) throws 
	UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
	    File file = new File(sDir);
	    File fFileToRename = null;
        String sFileName = new String();
        String sNewFileName = new String();
        String sNewTitle = new String();
        String sNewComment = new String();
        String sTouched = new String("@!");
        long lDate;
        boolean bUpdateTitle = false;
        boolean bRenameFile = false;
        boolean bResult ;
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
			        if (sFileName.toLowerCase().contains(".tmp"))
			        {
			        	bResult = fCurrentFile.delete();
			        }
			        else if (sFileName.toLowerCase().contains(".mp3") ){
	                	// Date de dernière modification => a mettre en préfixe dans les titres et nom du fichier
			        	lDate = fCurrentFile.lastModified();
			        	c.setTimeInMillis(lDate);
			        	
			        	// renommage du fichier
		            	// déjà mis à jour ! 
			        	bRenameFile = !sFileName.toLowerCase().contains(sTouched);
/*
			        	if (bRenameFile)
			        	{
				        	sNewFileName = String.format("%02d %02d %02d %s %s", c.get(Calendar.YEAR)-2000, 
				        			c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), sTouched, sFileName);
				        	System.out.println(fCurrentFile.getName() + " => " + sDir + sNewFileName);
				        	fFileToRename = new File(sDir + sNewFileName+".tmp");
			        	}
*/
			    		// renommage du titre 
			            Mp3File mp3file = new Mp3File(sDir + sFileName);
			            System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
			            System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
			            System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));

			            // Sur Tag V1
			            if (mp3file.hasId3v1Tag()) {
			            	ID3v1 id3v1Tag = mp3file.getId3v1Tag();
			            	System.out.println("Title: " + id3v1Tag.getTitle());
			            	System.out.println("Comment: " + id3v1Tag.getComment());
			            	// déjà mis à jour <=> préfixe du commentaire ! 
			            	bUpdateTitle = !id3v1Tag.getComment().toLowerCase().contains(sTouched);
			            	if (bUpdateTitle)
			            	{
					        	sNewTitle = String.format("%02d %02d %02d %s", c.get(Calendar.YEAR)-2000, 
					        			c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), id3v1Tag.getTitle());
					        	sNewComment = String.format("%s %s", sTouched, id3v1Tag.getComment());
					    		jtLog.setCaretPosition(jtLog.getDocument().getLength());
					    		jtLog.append("Titre id3v1Tag - " + id3v1Tag.getTitle() + " => " + sNewTitle + "\n");
					    		jtLog.setCaretPosition(jtLog.getDocument().getLength());
					    		jtLog.append("Comment id3v1Tag - " + id3v1Tag.getComment() + " => " + sNewComment + "\n");
					            id3v1Tag.setTitle(sNewTitle);
					            id3v1Tag.setComment(sNewComment);
			            	}
			            	else
			            	{
					    		jtLog.setCaretPosition(jtLog.getDocument().getLength());
					    		jtLog.append("Titre id3v1Tag - " + id3v1Tag.getTitle() + "\n");
					    		jtLog.setCaretPosition(jtLog.getDocument().getLength());
					    		jtLog.append("Comment id3v1Tag - " + id3v1Tag.getComment() + "\n");
			            	}
				            
			            }
			            
			            if (mp3file.hasId3v2Tag()) {
			            	ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			            	System.out.println("Title: " + id3v2Tag.getTitle());
			            	System.out.println("Comment: " + id3v2Tag.getComment());
			            	// déjà mis à jour <=> préfixe du commentaire ! 
			            	bUpdateTitle = !id3v2Tag.getComment().toLowerCase().contains(sTouched);
			            	if (bUpdateTitle)
			            	{
					        	sNewTitle = String.format("%02d %02d %02d %s", c.get(Calendar.YEAR)-2000, 
					        			c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), id3v2Tag.getTitle());
					        	sNewComment = String.format("%s %s", sTouched, id3v2Tag.getComment());

					        	jtLog.setCaretPosition(jtLog.getDocument().getLength());
					    		jtLog.append("id3v2Tag - " + id3v2Tag.getTitle() + " => " + sNewTitle + "\n");
					    		jtLog.setCaretPosition(jtLog.getDocument().getLength());
					    		jtLog.append("Comment id3v1Tag - " + id3v2Tag.getComment() + " => " + sNewComment + "\n");
					            id3v2Tag.setTitle(sNewTitle);
					            id3v2Tag.setComment(sNewComment);
			            	}
			            	else
			            	{
					    		jtLog.setCaretPosition(jtLog.getDocument().getLength());
					    		jtLog.append("Titre id3v2Tag - " + id3v2Tag.getTitle() + "\n");
					    		jtLog.setCaretPosition(jtLog.getDocument().getLength());
					    		jtLog.append("Comment id3v2Tag - " + id3v2Tag.getComment() + "\n");
			            	}
			            }

			            
			    		if (!bSimulation)
			    		{
			            	// vérifie si déjà mis à jour ! 
			    			if (bUpdateTitle)
			    			{
					    		jtLog.setCaretPosition(jtLog.getDocument().getLength());
					    		jtLog.append("Update Titre fichier" + sDir + sFileName + "\n");
				        		// Sauver le MP3File avec le nouveau nom
					    		if (bRenameFile)
				        		{
						        	sNewFileName = String.format("%02d %02d %02d %s %s", c.get(Calendar.YEAR)-2000, 
						        			c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), sTouched, sFileName);
						        	System.out.println(fCurrentFile.getName() + " => " + sDir + sNewFileName);
							    	jtLog.setCaretPosition(jtLog.getDocument().getLength());
							    	jtLog.append(fCurrentFile.getName() + " => MP3File " + sDir + sNewFileName + "\n");
				        			mp3file.save(sDir + sNewFileName);
				        			bResult = fCurrentFile.delete();
				        		}
				        		// Le nom du fichier a déjà été retouché => on sauve le MP3File avec un .tmp
					    		// on détruit le fichier retouché puis on renome le .tmp avec le même nom
				        		else
				        		{
//						        	fFileToRename = new File(sDir + sNewFileName+".tmp");
//					        		fCurrentFile.renameTo(fFileToRename);
							    	jtLog.setCaretPosition(jtLog.getDocument().getLength());
							    	jtLog.append(fCurrentFile.getName() + " détruit => sauvegarde MP3File " + sDir + sFileName + "\n");
					        		mp3file.save(sDir + sFileName + ".tmp");
				        			bResult = fCurrentFile.delete();
					        		fCurrentFile = new File(sDir + sFileName + ".tmp");
					        		fFileToRename = new File(sDir + sFileName);
					        		bResult = fCurrentFile.renameTo(fFileToRename);
				        		}
				            	
			    			}
			            	// vérifie si déjà mis à jour ! 
/*			        		if (bRenameFile)
			        		{
				    			fCurrentFile.renameTo(fFileToRename);
					    		jtLog.setCaretPosition(jtLog.getDocument().getLength());
					    		jtLog.append(fCurrentFile.getName() + " => " + sDir + sNewFileName + "\n");
			        		}
*/			        		
			    		}
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