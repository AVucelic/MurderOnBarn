# Murder on Barn

**Murder on Barn** is an engaging multiplayer game built in Java that pits players against each other in a high-stakes game of deception and teamwork. Inspired by the popular game "Among Us," players are divided into two roles: Crewmates and an Impostor. The game combines strategy, communication, and task management to create a thrilling experience.

## Overview

In **Murder on Barn**, players are assigned one of two roles:
- **Impostor**: The Impostor's objective is to eliminate all Crewmates without getting caught.
- **Crewmates**: The Crewmates must complete various tasks around the map to achieve victory while avoiding being killed by the Impostor.

## Features

- **Multiplayer Gameplay**: Play with friends or other online players in a dynamic server-client environment.
- **Impostor Role**: The Impostor can sabotage tasks and eliminate Crewmates to achieve their goal of total dominance.
- **Crewmate Tasks**: Complete a variety of tasks to ensure victory, including:
  - Swiping a Card
  - Connecting Wires
  - Downloading Data
  - Summing Numbers

## How to Play

1. **Setup**:
   - Ensure you have Java installed on your machine.
   - Clone or download the repository from GitHub.

2. **Running the Game**:
   - Open a terminal or command prompt.
   - Navigate to the project directory.
   - Compile the Java files using `javac`:
     ```bash
     javac -d bin src/*.java
     ```
   - Run the server:
     ```bash
     java -cp bin Server
     ```
   - Run the client for each player:
     ```bash
     java -cp bin Client
     ```

3. **Playing**:
   - Join a game server or host your own.
   - Follow the on-screen instructions to complete tasks or deceive other players.
   - Communicate with other players to strategize and identify the Impostor.

## Tasks Overview

- **Swipe the Card**: Drag the card through the reader to match the speed and complete the task.
- **Connect the Wires**: Match and connect wires of the same color to complete the circuit.
- **Download Data**: Wait for the data to download while defending yourself from the Impostor.
- **Sum Numbers**: Solve simple arithmetic problems to finish the task.

## Contributing

We welcome contributions to enhance the game. To contribute:
1. Fork the repository.
2. Create a new branch for your changes.
3. Make your changes and test thoroughly.
4. Submit a pull request with a detailed description of your changes.

## Issues

If you encounter any issues or bugs, please report them on our [GitHub Issues](https://github.com/yourusername/murder-on-barn/issues) page. Provide as much detail as possible to help us address the problem.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Inspired by "Among Us."
- Thanks to the open-source community for libraries and tools used in development.

Enjoy the game and may the best team win!

