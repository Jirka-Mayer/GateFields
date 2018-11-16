Akce editoru
============

Akce editoru se nějak spustí a ve chvíli kdy běží si přebere kontrolu nad editorem.

Možnosti jak spustit akci:
- LMB mouse down (např. pohyb kamery po scéně)
- keydown (všechny možné jiné akce jako je přidávání objektů, ...)

`Event` reprezentuje událost, která vznikla. Eventy vytváří a posílá
do aplikace `EventDispatcher`. Eventy bude hlídat `EventListener`.

Akce se registruje do nějaké třídy, která bude držet seznam možných akcí `ActionController`.
Akce má zaregistrovaný `EventListener` na spuštění akce.

Akce se spustí voláním `actionControllerInstance.startAction(action)`.

Akce se do kontrolleru předá jako instance, aby si mohla požádat o závislosti ve svém
konstruktoru a `ActionController` je tak nemusel řešit. 

Některé akce mohou mít efekt i bez spuštění (jakože se ukončí jakmile se spustí).
Například zoom. Ale to uděláme jako akci co se hned sama ukončí.

> TODO:
> Napiš akci pro pohyb editoru a otestuj ji posíláním fake událostí do `EventListener`
