package routing.QL;



import java.util.Random;


public class BoltzmannExploration implements IExplorationPolicy {
	double temperature;
	private Random r = new Random();

	/**
	 * Initializes a new instance of the BoltzmannExploration class.
	 * 
	 * @param temperature Temperature parameter of Boltzmann distribution.
	 */
	public BoltzmannExploration(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * If temperature is low, then the policy tends to be more greedy.
	 * 
	 * @return Temperature
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * The property sets the balance between exploration and greedy actions.
	 * 
	 * @param temperature Temperature
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * The method chooses an action depending on the provided estimates. The
	 * estimates can be any sort of estimate, which values usefulness of the action
	 * (expected summary reward, discounted reward, etc).
	 * 
	 * @param actionEstimates Action Estimates.
	 * @return Return selected action.
	 */
	@Override
	public int ChooseAction(double[] actionEstimates, boolean[] actionRestriction) {
		// actions count
		int actionsCount = actionEstimates.length;
		// action probabilities
		double[] actionProbabilities = new double[actionsCount];
		// actions sum
		double sum = 0, probabilitiesSum = 0;
		// System.out.println(temperature);
		for (int i = 0; i < actionsCount; i++) {

			double actionProbability = Math.exp(actionEstimates[i] / temperature);

			actionProbabilities[i] = actionProbability;
			probabilitiesSum += actionProbability;

		}

		if ((Double.isInfinite(probabilitiesSum)) || (temperature == 0)) {
			// do greedy selection in the case of infinity or zero
			double maxReward = actionEstimates[0];
			int greedyAction = 0;
			int start = 0;
			for (int i = 0; i < actionsCount; i++) {
				if (actionRestriction[i] == true) {
					{
						maxReward = actionEstimates[i];
						greedyAction = i;
						start = i + 1;
					}

				}
			}
			for (int i = start; i < actionsCount; i++) {
				if (actionRestriction[i] == true) {
					if (actionEstimates[i] > maxReward) {
						maxReward = actionEstimates[i];
						greedyAction = i;
					}
				}

			}

			return greedyAction;
		}

		// get random number, which determines which action to choose
		double actionRandomNumber = r.nextDouble();

		for (int i = 0; i < actionsCount; i++) {
			if (actionRestriction[i] == true) {
				sum += actionProbabilities[i] / probabilitiesSum;
				if (actionRandomNumber <= sum)
					return i;
			}

		}
		int a = 0;
		for (int i = (actionsCount - 1); i >= 0; i--) {
			if (actionRestriction[i] == true) {
				a = i;
			}
		}
		return a;
	}
}
