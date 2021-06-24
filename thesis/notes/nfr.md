### NFR
* Intro
	- reliable 
	- scalable 
	- extensibility
	- reusability
	- maintainability
	- run in cost effective manner
	- aliases: cross-functional requirements / system attributes
* Code Quality
	- team should have same coding style guidelines
	- nf perspective: reducing time-to-context (readability of the code)
	- free of defecies (scalability, hande faults gracefully)
* Architectural Quality
	- check dependencies between packages and classes, layers and slices, check for cyclic dependencies etc.
	- insignificant
* Security, Vulnerabilities, and Threats
	- insignificant
* Chaos Testing
	- netflix chaos monkey
	- "chaos engineering is fundamentally the discipline of experimenting on distributed system in order to build confidence in the system's capability to withstand turbulent conditions in production

### Kriterienkatalog
* Performance
	1. throughput per sec (av, max / peak)
* Volumes
	1. number of transactions (average, max)
* Flexibility: Moeglichkeit weiter zu entwickeln
	1. maintainability
	2. portability
	3. configurability
	4. testability
* Availability
	4. disaster recoverty plan
	5. exception handling and logging
	6. recoverability
* Scalability
	2. Scaling dimension
* development
	3. software frameworks / servers
