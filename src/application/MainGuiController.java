package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class MainGuiController {
	@FXML
	private TextField tfDir;
	@FXML
	private TextArea taProcess;
	private File rootFile;

	@FXML
	private void btSelecionarAction(Event evt){
		DirectoryChooser dChooser = new DirectoryChooser();
		dChooser.setInitialDirectory(new File("./"));
		rootFile = dChooser.showDialog(null);
		tfDir.setText(rootFile.getAbsolutePath());
	}

	@FXML
	private void btDeletarArquivosAction(Event evt){
		String deleted = new String("");
		String[] listSolic = taProcess.getText().split("\n");

		File[] list = rootFile.listFiles();
		for (int i = 0; i < list.length; i++) {
			for (int j = 0; j < listSolic.length; j++) {
				if (list[i].getName().equals(listSolic[j])) {
					deleted = deleted + list[i].getName() + "\n";
					list[i].delete();
					break;
				}
			}
		}
		taProcess.appendText("===========================================================\n" + deleted);
	}

	@FXML
	private void btListarArquivosAction(Event evt){
		String ret = recursivePrint(rootFile.listFiles(), 0, "");
		taProcess.setText(ret);
		recursiveDelete(rootFile.listFiles(), 0);
	}

	@FXML
	private void btMoverArquivosAction(Event evt){
		recursiveMover(rootFile.listFiles(), 0, rootFile);
	}


    private static String recursivePrint(File[] arr,int index, String in)
    {
    	String ret = new String("");
    	ret = ret + in;

        // terminate condition
        if(index == arr.length)
            return ret;

        // for files
        if(arr[index].isFile())
            ret = ret + arr[index].getName() + "\n";

        // for sub-directories
        else if(arr[index].isDirectory())
        {
            // recursion for sub-directories
            ret = recursivePrint(arr[index].listFiles(), 0, ret);
        }
        ret = recursivePrint(arr,++index, ret);
        return ret;
   }

    private void recursiveMover(File[] arr,int index, File root)
    {
        // terminate condition
        if(index == arr.length)
            return;

        // for files
        if(arr[index].isFile()){
        	if(!arr[index].getAbsolutePath().equals(root.getAbsolutePath())){
        		taProcess.appendText("\n"+arr[index].getAbsoluteFile() + " - " + arr[index].getName() + " - Moved");
        		arr[index].renameTo(new File(root.getAbsolutePath() + "\\" + arr[index].getName()));
        	}
        }
        // for sub-directories
        else if(arr[index].isDirectory())
        {
            // recursion for sub-directories
        	recursiveMover(arr[index].listFiles(), 0, root);
        }
        recursiveMover(arr,++index, root);
   }

    private void recursiveDelete(File[] arr,int index)
    {
        // terminate condition
        if(index == arr.length)
            return;

        // for sub-directories
        else if(arr[index].isDirectory())
        {
        	if(arr[index].list().length == 0){
        		taProcess.appendText("\nDelete: " + arr[index].getAbsolutePath());
        		arr[index].delete();
        	}else{
        		recursiveDelete(arr[index].listFiles(), 0);
            	if(arr[index].list().length == 0){
            		taProcess.appendText("\nDelete: " + arr[index].getAbsolutePath());
            		arr[index].delete();
            	}
        	}
        }
        recursiveDelete(arr,++index);
   }
}
