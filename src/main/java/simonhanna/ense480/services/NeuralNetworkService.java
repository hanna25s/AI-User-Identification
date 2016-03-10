package simonhanna.ense480.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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
	
	public static double identifyUser(List<KeyMetric> keyMetrics) {
		return 0.0;
	}
	
	public static void trainNeuralNetwork(Profile profile) {
		List<Profile> negativeReinfocementProfiles = DatabaseService.getOtherProfiles(profile);
		List<MLDataPair> data = new ArrayList<MLDataPair>();
		
		data.add(getDataPair(profile.getKeyMetrics(), VALID_USER));
		
		BasicNetwork network = EncogUtility.simpleFeedForward(100, 12, 0, 1, false);
        network.reset();
		
		negativeReinfocementProfiles.forEach(tempProfile -> {
			List<KeyMetric> tempKeyMetrics = tempProfile.getKeyMetrics();
			if(tempKeyMetrics != null && tempKeyMetrics.size() != 0)
				data.add(getDataPair(tempKeyMetrics, INVALID_USER));
		});
		
		MLDataSet trainingSet = new BasicMLDataSet(data);    
        final Backpropagation train = new Backpropagation(network, trainingSet, 0.07, 0.6);
        train.setBatchSize(1);
        EncogUtility.trainToError(train, 0.005);
        
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(
            		new FileOutputStream("neuralNetworks/" + Integer.toString(profile.getProfileid()) + ".nn"));

            objectOutput.writeObject(network);
            objectOutput.close();

        } catch (Exception ex) {
        	ex.printStackTrace();
        }
	}
	
	public static void createNeuralNetwork(List<KeyMetric> keyMetrics, Profile profile) {
		
		//Create Network
		BasicNetwork network = EncogUtility.simpleFeedForward(100, 12, 0, 1, false);
        network.reset();
		
        MLDataSet trainingSet = getTrainingSet(keyMetrics, VALID_USER);
		
		// Train the neural network.
        final Backpropagation train = new Backpropagation(network, trainingSet, 0.07, 0.02);
        train.setBatchSize(1);
		
        // Evaluate the neural network.
        EncogUtility.trainToError(train, 0.01);
        EncogUtility.evaluate(network, trainingSet);
        
        
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(
            		new FileOutputStream("neuralNetworks/" + Integer.toString(profile.getProfileid()) + ".nn"));

            objectOutput.writeObject(network);
            objectOutput.close();

        } catch (Exception ex) {
        	ex.printStackTrace();
        }
	}
	
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
	
	public static double evaluateNeuralNetwork(BasicNetwork network, List<KeyMetric> keyMetrics) {
		
		if(network == null) {
			System.out.println("Network is null");
			return -1;
		}
		
		MLDataPair dataPair = new BasicMLDataPair(new BasicMLData(getInputArray(keyMetrics)));
		System.out.println(Double.toString(dataPair.getInput().size()));
		return network.compute(dataPair.getInput()).getData(0);
		
	}
	
	private static MLDataSet getTrainingSet(List<KeyMetric> keyMetrics, double[] output) {
		
		double[] input = getInputArray(keyMetrics);
		
		MLDataPair dataPair = new BasicMLDataPair(new BasicMLData(input), new BasicMLData(output));
		List<MLDataPair> data = new ArrayList<MLDataPair>();
		data.add(dataPair);
		
		MLDataSet trainingSet = new BasicMLDataSet(data);
		
		return trainingSet;
		
	}
	
	private static MLDataPair getDataPair(List<KeyMetric> inputs, double[] expectedOutput) {
		double[] inputArray = getInputArray(inputs);
		
		MLDataPair dataPair = new BasicMLDataPair(new BasicMLData(inputArray), new BasicMLData(expectedOutput));
		
		return dataPair;
	}
	
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
