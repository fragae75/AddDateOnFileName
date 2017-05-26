package com.flg.DateOnFileName;

import java.io.IOException;
import java.io.RandomAccessFile;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

//import java.awt.FileDialog;
//import javax.swing.JFileChooser;

// exe : http://stackoverflow.com/questions/8774546/making-exe-file-for-java-project-containing-sqlite
/*
 * 
 * Renomme les MP3 en leur prefixant leur date fr modification
 * 
 */
public class DateOnFileName {

	static FenetreAddDateOnFileName fenetreAddDateOnFileName;
	static boolean bSimulation = false;
	static String sRepertoire = "C:\\Temp\\Podcasts\\";
	static TraiteFichier traiteFichier = new TraiteFichier();
	
	public static void main(String[] args) {

		fenetreAddDateOnFileName = new FenetreAddDateOnFileName();

	}

}



/*	
JFileChooser c = new JFileChooser();
String filename = new String();
String dir = new String();

// Demonstrate "Open" dialog:
int rVal = c.showOpenDialog(null);//  .showOpenDialog(DateOnFileName.this);
if (rVal == JFileChooser.APPROVE_OPTION) {
  filename = c.getSelectedFile().getName();
  dir = c.getCurrentDirectory().toString();
}
if (rVal == JFileChooser.CANCEL_OPTION) {
  filename = "You pressed cancel";
  dir = "";
}
*/
