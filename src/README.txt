Tema2
Planificarea de task-uri intr-un datacenter

Clasele MyDispatcher.java si MyHost.java implementeaza clasele abstracte
Dispatcher si Host.

MyDispatcher:
	Datacentr-ul primeste input-uri pe baza carora se decide
	ce algorit este folosit pentru a distribui task-urile

	Algoritmii implementati sunt Round Robin, Shortest Queue,
	SITA si LWL.
MyHost:
	Aceasta reprezinta clasa care se ocupa cu rezolvarea task-urilor
	implementarea se bazeaza pe o coada de prioritati in care
	sunt adaugate elementele primite de la datacenter.
	Din coada de prioritati sunt extrase mai intai task-urile
	cu prioritatea mare. Task-urile se rezolva in ordine, mai 
	putin in situatia in care cel ce se afla in desfasurare este 
	preemptabil si exista in coada un task cu o prioritate mai mare,
	in acest caz task-ul in desfasurare este amanat.