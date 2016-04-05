package simonhanna.ense480.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.util.simple.EncogUtility;

import simonhanna.ense480.models.KeyMetric;
import simonhanna.ense480.models.Profile;

public final class NeuralNetworkService {
	
	public static double[] VALID_USER =  {1.0};
	public static double[] INVALID_USER = {0.0};
	
	/**
	 * Grabs the neural networks from the neuralNetwork file and runs them all
	 * against the given metrics to see which user they belong to.
	 * 
	 * @param keyMetrics   Metrics we wish to find a user for
	 * @return  Returns the profile the neural network believes the metrics belong to
	 */
	public static Profile identifyUser(List<KeyMetric> keyMetrics) {
		
		BasicNetwork network = null;
		String profileId = null;
		ObjectInputStream ois = null;
		double highestValue = 0;
		double tempValue = 0;
		
		File[] neuralNetworks = new File("neuralNetworks/").listFiles(new FilenameFilter() { 
	         	public boolean accept(File dir, String filename)
             		{ return filename.endsWith(".nn"); }
	         });
		
		/*
		 * Loop through every trained neural network we have. Since we are using the least hated selection algorithm,
		 * we simply check to see if the current network evaluates to a more accurate value than the previous. If it
		 * does, we simply update the detect profile to the current, and adjust the current highest value
		 */
		for(int i=0; i<neuralNetworks.length; i++) {
			try {
				ois = new ObjectInputStream(new FileInputStream("neuralNetworks/" + neuralNetworks[i].getName()));
				network = (BasicNetwork) ois.readObject();
				tempValue = evaluateNeuralNetwork(network, keyMetrics);
				if(tempValue > highestValue) {
					highestValue = tempValue;
					profileId = neuralNetworks[i].getName();
					System.out.println("New closest: " + profileId);
					System.out.println(tempValue);
				}
			} catch (Exception e) {
				System.out.println("Error reading neural network: " + neuralNetworks[i].getName());
				e.printStackTrace();
			} 
		}
		
		try {
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(profileId == null)
			return null;
		
		profileId = profileId.substring(0, profileId.indexOf('.'));
		return DatabaseService.getProfileFromId(Integer.parseInt(profileId));

	}
	
	/**
	 * Used to create and train a neural network to identify a specific profile. The 
	 * neural network is then saved to a file for later use
	 * 
	 * @param profile   Profile to be trained. By passing profile we also get the key metrics
	 */
	public static void trainNeuralNetwork(Profile profile) {
		List<Profile> negativeReinfocementProfiles = DatabaseService.getOtherProfiles(profile);
		List<MLDataPair> data = new ArrayList<MLDataPair>();
		
		BasicNetwork network = EncogUtility.simpleFeedForward(100, 12, 0, 1, false);
        network.reset();
		
		negativeReinfocementProfiles.forEach(tempProfile -> {
			List<KeyMetric> tempKeyMetrics = tempProfile.getKeyMetrics();
			if(tempKeyMetrics != null && tempKeyMetrics.size() != 0)
				data.add(getDataPair(tempKeyMetrics, INVALID_USER));
		});
		
		data.add(getDataPair(profile.getKeyMetrics(), VALID_USER));
		
		MLDataSet trainingSet = new BasicMLDataSet(data);    
        final Backpropagation train = new Backpropagation(network, trainingSet, 0.07, 0.6);
        train.setBatchSize(1);
        EncogUtility.trainToError(train, 0.01);
        
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(
            		new FileOutputStream("neuralNetworks/" + Integer.toString(profile.getProfileid()) + ".nn"));

            objectOutput.writeObject(network);
            objectOutput.close();

        } catch (Exception ex) {
        	ex.printStackTrace();
        }
	}
	
	/**
	 * Simply a loop to call the trainNeuralNetworks method for all existing profiles
	 */
	public static void trainAllNeuralNetworks() {
		DatabaseService.getProfiles().forEach(profile -> {
			System.out.println("Training: " + profile.getProfilename());
			trainNeuralNetwork(profile);
		});
	}
	
	/**
	 * Used to get neural networks from files to be used for evaluating metrics
	 * 
	 * @param profileId   Lets us know which profile's neural network we want
	 * @return   Returns the trained neural network for that specific profile
	 */
	public static BasicNetwork readNeuralNetworkFromFile(int profileId) {
		BasicNetwork network = null;
		
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream("neuralNetworks/" + Integer.toString(profileId) + ".nn"));
			
			network = (BasicNetwork) ois.readObject();
			ois.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return network;
	}
	
	/**
	 * Used to return the result of a neural network. The function assumes that the neural network provided
	 * has a single output. Inputs and hidden layers can be any number as long as the provided number of 
	 * key metrics matches the networks number of inputs.
	 * 
	 * @param network     Network to run the key metrics against
	 * @param keyMetrics  Key metrics to use an inputs
	 * @return   Output of given network using provided key metrics as inputs
	 */
	public static double evaluateNeuralNetwork(BasicNetwork network, List<KeyMetric> keyMetrics) {
		
		if(network == null) {
			System.out.println("Network is null");
			return -1;
		}
		
		MLDataPair dataPair = new BasicMLDataPair(new BasicMLData(getInputArray(keyMetrics)));
		return network.compute(dataPair.getInput()).getData(0);
		
	}
	
	/**
	 * Converts a list of KeyMetrics and the corresponding expected output into a data pair that
	 * can be used to train a neural network
	 * 
	 * @param inputs           Inputs we wish to train for
	 * @param expectedOutput   Output expected when using given inputs
	 * @return   A data pair created using the given inputs and expected outputs
	 */
	private static MLDataPair getDataPair(List<KeyMetric> inputs, double[] expectedOutput) {
		double[] inputArray = getInputArray(inputs);
		
		MLDataPair dataPair = new BasicMLDataPair(new BasicMLData(inputArray), new BasicMLData(expectedOutput));
		
		return dataPair;
	}
	
	/**
	 * Converts key metric list to an array of doubles, which is the format Encog uses for neural network inputs
	 * 
	 * @param keyMetrics   Key metrics you wish to convert
	 * @return  Provided key metrics as an array of doubles
	 */ 
	private static double[] getInputArray(List<KeyMetric> keyMetrics) {
		double[] input = new double[100];
		
		keyMetrics.forEach(km -> {
			if(km.getNumberOfOccurences() == 0) {
				input[km.getFrom() + 10 * km.getTo()] = 0.0;
			} else {
				input[km.getFrom() + 10 * km.getTo()] = km.getTime() / km.getNumberOfOccurences();
			}
		});
				
		return input;
	}
}
