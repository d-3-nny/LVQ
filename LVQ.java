/* LVQ for pattern clustering
 * Copyright: Denny Hermawanto - 2006
 * Mail: d_3_nny@yahoo.com
 *
 *  Suppose we have data patterns (1;1;0;0),(0;0;0;1),(0;0;1;0),(1;1;0;0). 
 *  We want to assign the patterns to two classes:
 *
 *    Vector      Class
 *   (1;1;0;0)      0
 *   (0;0;0;1)      1
 *   (0;0;1;0)      0
 *   (1;1;0;0)      0
 */
 
public class LVQ{

	private void DefineInput(){
		inputvector = new double[numberofinput][inputdimension];
		
		inputvector[0][0] = 1;
		inputvector[0][1] = 1;
		inputvector[0][2] = 0;
		inputvector[0][3] = 0;

		inputvector[1][0] = 0;
		inputvector[1][1] = 0;
		inputvector[1][2] = 0;
		inputvector[1][3] = 1;

		inputvector[2][0] = 0;
		inputvector[2][1] = 0;
		inputvector[2][2] = 1;
		inputvector[2][3] = 0;

		inputvector[3][0] = 1;
		inputvector[3][1] = 1;
		inputvector[3][2] = 0;
		inputvector[3][3] = 0;
	}
	
	private void DefineCluster(){
		targetcluster = new int[numberofinput];
		targetcluster[0] = 0;
		targetcluster[1] = 1;
		targetcluster[2] = 0;
		targetcluster[3] = 0;
	}
	
	private double RandomNumberGenerator(){
		java.util.Random rnd = new java.util.Random();
		return rnd.nextDouble();
	}
	
	private double LearningRateDecay(double currentlearningrate){
		double result = 0;
		result = 0.8 * currentlearningrate;
		return result;
	}
	
	private void InitializeWeigths(){
		weights = new double[numberofcluster][inputdimension];
		for(int i=0;i<numberofcluster;i++){
			for(int j=0;j<inputdimension;j++){
				weights[i][j] = RandomNumberGenerator();
			}
		}
	}
	
	private double ComputeEuclideanDistance(double[] vector1, double[] vector2){
		double result;
		double distance =0;
		for(int j=0;j<inputdimension;j++){
			distance += Math.pow((vector1[j] - vector2[j]), 2);
		}
		result = distance;
		return result;
	}
	
	private void TrainLVQ(int maxiteration){
		euclideandistance = new double[numberofcluster];
		System.out.print("Training LVQ");
		for(int iter=0;iter<maxiteration;iter++){
			for(int k=0;k<numberofinput;k++){
				//Get the winning neuron
				winningneuron = 0;
				for(int i=0;i<numberofcluster;i++){
					euclideandistance[i] = ComputeEuclideanDistance(weights[i],inputvector[k]);
					if(i!=0){
						if(euclideandistance[i]<euclideandistance[winningneuron]){
							winningneuron = i;
						}
					}
					//System.out.println(euclideandistance[i]);
				}
				if(targetcluster[k] == winningneuron){
					for(int i=0;i<inputdimension;i++){
						weights[winningneuron][i] += learnrate * (inputvector[k][i] - weights[winningneuron][i]);
					}
				}
				else{
					for(int i=0;i<inputdimension;i++){
						weights[winningneuron][i] = weights[winningneuron][i] - (learnrate * (inputvector[k][i] - weights[winningneuron][i]));
					}
				}
				
				//System.out.println("Winner:"+winningneuron);
				//Update the winning neuron
			}
			learnrate = LearningRateDecay(learnrate);
			System.out.print(".");	
			//System.out.println("Learn Rate:"+learnrate);
		}
	}
	
	private void MappingInputVector(){
		System.out.println("\n \n"+"Mapping Input Vectors:");
		for(int k=0;k<numberofinput;k++){
			winningneuron = 0;
			for(int i=0;i<numberofcluster;i++){
				euclideandistance[i] = ComputeEuclideanDistance(weights[i],inputvector[k]);
				if(i!=0){
					if(euclideandistance[i]<euclideandistance[winningneuron]){
						winningneuron = i;
					}
				}
			//System.out.println(euclideandistance[i]);
			}
			System.out.println("Input["+k+"] -> Cluster No:"+winningneuron);
		}
	}
	
	public void RunLVQ(){
		DefineParameters();
		DefineInput();
		DefineCluster();
		InitializeWeigths();
		TrainLVQ(50);
		MappingInputVector();
	}
	
	public void DefineParameters(){
		numberofcluster = 2;
		inputdimension = 4;
		numberofinput = 4;
		learnrate = 0.6;
	}
	
	public static void main(String[] args){
		LVQ lvq = new LVQ();
		lvq.RunLVQ();
	}
	
	//define variables
	private double[][] inputvector;
	private double[][] weights;
	private double[] euclideandistance;
	private int[] targetcluster;
	private int numberofcluster;
	private int inputdimension;
	private int numberofinput;
	private double learnrate;
	private int winningneuron;
}