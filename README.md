# Assumptions and specifications of the solution
1. Rockets and Missions are uniquely identified by their names. This means that two rockets cannot have the same name. The same applies to missions.
2. In the task there were two points about changing statuses of rockets and mission.
   In section 8 it is clearly stated that only two statuses might be influenced by outside input:
   - the fact that Rocket might come in/out of status __In repair__
   - the fact that Mission might become __Ended__
    
   All other statuses of rockets and missions have clearly stated automatic transitions. 
   Taking this fact into consideration, in the SpaceXDragonRepository class I have created the following methods:
   - _putRocketIntoRepair_ / _completeRepairOfRocket_
   - _endMission_
 3. Using the _module-info.java_ I have only allowed usage of SpaceXDragonRepository methods from this library

# Usage of AI models
- help with PR reviews using Gemini Code Assist
- help with creating documentation
- help with creating tests