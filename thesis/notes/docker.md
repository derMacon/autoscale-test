# Docker notes

## Einleitung
* docker ueberbrueckt klare Trennung zwischen Development und Operations Team
* images als Abstraktionlayer 
	- enthalten bereits alle Dependencies
	- fuer operations engineer sehen alle images gleich aus
	- moeglich Deployment dadurch zu automatisieren
* Aufbau
	- client & server benutzen selbe executable (Flag zum Unterscheiden)
	- Docker Server / Daemon
		- direkte Kommunikation mit Registry
		- startet container
		- nimmt Befehle vom client entgegen
		- Kommunikation ueber Netzwerk Sockets
	- mit clients sind die laufenden Container gemeint
	- cli tool "docker" wesentliche Steuerungs-Schnittstelle

## Docker Workflow
* Filesystem layers
	- Container bestehen aus gestapelten Dateisystem Schichten
	- einzigartiger Hash zum Identifizieren der Schichten
	- build Prozess generiert eine neue Schicht mit jeder Aenderung die vorgenommen wird
	- Rebuild braucht nur die geanderten Schichten anzupassen

## Docker vs. VM
- Generell: 
	- "think of containers not as virtual machines, but as very lightweight wrappers around a single Unix process"
	- Container sind ausserdem sehr kurzlebig
	- Container sind stateless 
		- immutable Infrastruktur macht es einfach Aenderungen an der Konfiguration vorzunehmen
		- man redeployt einfach die Container 
		- wenn der Daemon geupdated werden soll, verschiebt man Container auf neuen Daemon
	- Container Isolation
		- Default Config: Container konkurrieren um Resourcen
		- moeglich diese aehnlich wie bei VMs pro Container zu begrenzen
	- wesentlicher Unterschied zu VM: Tiefe der Isolation
		- Container sind Prozesse die auf Docker Server laufen
		- laufen auf genau gleicher Instanz vom Linux Kernel wie Hostsystem
		- VMs erstellen eine komplett eigene Instanz eines Betriebssystems <--- nochmal nachschauen was das im Detail heisst
	* Container state
	- Umgebungsvariablen werden in den Metadaten der Container-Konfiguration gespeichert
- Kernel
	- VMware: "run a complete Linux kernel and operating system on top of a virtualized layer"
	- ermoeglicht starke Isolation zwischen VMs, da jeder gehostete Kernel in seperatem memory space sitzt
	- Container laufen alle im gleichen Kernel -> "this is called operating system virtualization"
	- Zitat: A container is a self-contained execution environment that	shares the kernel of the host system and which is (optionally) isolated from other containers in the system."
	- Docker nimmt im Vergleich zu einer VM eine Abstraktionsstufe zwischen der isolierten Task und der tatsaechlichen Hardware, da der Container direkt auf dem Kernel laeuft
	- Nachteil Docker: Prozesse muessen auf Ausfuehrung im Kernel ausgelegt sein (Windows Container koennen nicht auf Linux Kernel laufen)

## Netzwerk
- Container verhalten sich wie Hosts in einem privaten Netzwerk
- Daemon verhaelt sich wie eine virtuelle Bruecke 
- Bruecke leitet traffic ins virtuelle mini Netzwerk der Hosts
- Jeder Container hat eigenes virtuelles Ethernet interface, welches sich mit der Docker bridge verbindet
- Jedes Interface allokiert eigene IP Adresse
- moeglich Container Port der Aussenwelt zur Verfuegung zu stellen
	- dieser Traffic laeuft ueber einen Proxy (Teil des Docker Daeomon)
- Docker allokiert ein privates Subnetz 
- Netzwerk Lifecycle sehr gut zusammengefasst 
	-> S. 13 (unterer Part) - S. 14
- WICHTIG (S. 63 - Hostname): beim Start werden bestimmte Config Dateien in eine Docker config directory auf dem Host kopiert
	- umfasst Dateien wie /etc/hostname etc. 
	- container mounten diese Directory in ihr eigenes Filesystem

## Noisy neighbours
- Docker unterstuetzt die Begrenzung von CPU, momory und swarm limits, dies muss allerdings im Kernel aktiviert werden
- S. 68: CPU Shares gut erklaert
	- CPU Zeit als Pool mit 1024 Shares betrachtet
	- Containern kann werden anhand der gegebenen Anzahl vom Scheduler angesprochen
	- 1024 Shares bedeutet nicht den exclusiven Zugriff fuer einen entsprechenden Container
	- laeuft ueber die cgroup
		- "Cgroups allow you to allocate resources — such as CPU time, system memory, network bandwidth, or combinations of these resources — among user-defined groups of tasks (processes) running on a system"

=> Aktueller Stand: S. 90 / 222 (bzw. S. 68 im PDF)
