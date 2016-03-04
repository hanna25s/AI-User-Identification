# AI-User-Identification
This is a system that will try to identify users based on their key strokes. This is done using Key Metrics. The program will identify when a user hits various keys, and create a profile based on time between keys. These prfoiles will then be used to train an Artificial Neural Network to identify each user.

The Neural Network created will be a feedforward network using back propogation for training. Each network will be trained specifically to detect one user. This will be done by using positive reinforcement while training with one specific user, and negative while training againts the remaining users.

This project will follow the ideas proposed by the paper "Security Through Behavioral Biometrics and Artificial Intelligence" written by Benjamin Purgasona and David Hiblerb.

#Dependencies
This project will make use of Maven for depency management. It will rely on Hibernate/JPA for database mapping and interaction. It will also use Encog as a Neural Network library.
