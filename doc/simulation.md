Simulace
========

Při simulaci se střídají dvě fáze. Nejdříve se simuluje šíření signálu skrz
dráty a potom po ustálení se zaktualizuje prioritní fronta změn výstupů hradel.


Hradla
------

Simulace hradel probíhá na základě prioritní fronty jako diskrétní simulace v čase.

Dojde-li ke změně signálu na vstupní straně hradla, tak si hradlo přepočítá nový výstup
a zařadí událost změny výstupu do budoucnosti. Tím se simuluje jeho zpoždění.

Čili vývody hradel lze dělit na vstupní a výstupní. Vstupní způsoují přepočítání výstupních.
Pokud signál vstupuje do výstupních, tak ho hradlo ignoruje.

Jenže někdy je potřeba mít výstupy, které se chovají někdy jako vstup a někdy jako výstup.
Například RS latch lze překlopit a potom se role vývodů vymění. Nebo pokud hradlo reprezentuje
nějakou zapouzdřenou hradlovou síť.

Takové chování si musí vyřešit implementace hradla správnou změnou rolí vývodů.

> Pokud uvažujeme zapouzdřené sítě, tak z pohledu vnitřní sítě jsou její vstupy a výstupy
pouze hradla, která se chovají někdy jako vstup a někdy jako výstup. Jejich přepínání
řídí logika zapouzdření, ale odstíním tím díky nim fáze šíření signálu dráty od vnější sítě,
což zjednoduší logiku šíření signálu.


### Implementace

Stav signálu v celém schématu drží třída `Simulator`.

Fronta simulace je ve třídě `SimulationQueue`. Do ní se přidávání události,
které jsou zavolány ve chvíli zpracování:

```java
SimulationQueue queue = new SimulationQueue();
queue.add(new SimulationEvent(() -> {
    // handle the event, register new events as a result
}));
```

Simulační fronta je součást simulátoru:

```java
SimulationQueue queue = simulator.getSimulationQueue();
```


Dráty
-----

Simulace signálu v drátech probíhá také pomocí fronty, ale neřeší se zde čas
nebo zpoždění signálu. Fáze doběhne ve chvíli, kdy se fronta vyprázdní - šíření
se ustálí. Kdyby probíhala simulace v čase, tak mi někde může zůstat signál běhající
ve smyčce, co produkuje signály do okolí.

Signál je vlastnost vrcholů. Aktivní vrchol je zdrojem signálu. Sousední vrchol se
stane zdrojem, když zjistí, že jeho soused je zdrojem.

Aby nevznikaly "zamrzlé" cykly, kde jsou si vrcholy zdrojem navzájem, přiřadím vrcholům
číslo vrstvy. Aktivní vrcholy budou ve vrstvě 0. Každý další vrchol, pokud se má stát zdrojem,
tak musí ukázat na souseda, který je zdrojem, ale v nižší vrstvě. Tím vznikne mezi
vrcholy uspořádání, které zamezí zamrznutí signálu v cyklu.

Při změně stavu nějakého vrcholu se do fronty přidají všechny jeho sousedi pro zkontrolování.

Stejně tak, pokud se změní stav vázaného vrcholu (k hradlu), tak se přidá hradlo do seznamu
hradel ke zkontrolování. Může se stát, že po přepočítání přijde signál od jinud a tak signál
z vývodu nezmizí. Potom se hradlo neaktualizuje. Ale může se stát, že se jedná o aktivní
vývod RS latche a potom se musí těmto drátům přepočítat nový zdroj - právě tenhle vrchol. 


### Implementace

Třída `Simulator` si drží seznam aktivních vrcholů.

Pokud chceme aktivovat vrchol, jednoduše to sdělíme simulátoru:

```java
simulator.activateVertex(vertex);
```

Jelikož může dojít ke změně většího počtu vrcholů, simulátor neprovede
šíření signálu, dokud mu explicitně neřekneme.

```java
simulator.propagateSignals();
```

Během tohoto volání napočítá, jak se změní stavy vrcholů, sestaví seznam probuzených hradel
a na těchto hradle zavolá metodu, aby si případně zaktualizovaly stav:

```java
element.signalsChanged(Simulator simulator);
```
