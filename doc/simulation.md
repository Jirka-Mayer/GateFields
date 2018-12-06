Simulace
========

Řekněme, že načtu shéma ze souboru. Tím se mi načte veškeré zapojení, seznam aktivních vrcholů
a vnitřní stavy hradel. Z aktivních vrcholů spočítám aktivní komponenty a jsem připravený.

Teď se ale můžu nacházet ve stavu, kde některá hradla mají nesmyslný výstup oproti vstupu.
Čili potom co vše načtu, tak projdu všechny hradla a naplánuju jim do fronty update.

> A teď vlastně už probíhá klasická simulační smyčka.

Vezmu z fronty události, co se staly. Každou událost vyhodnotím a podle toho
překlopím aktivity některých vrcholů.

Překlápění aktivit se bufferuje do paměti a hned se neprovádí, protože si nemůžu měnit
stav signálu v obvodu během jeho přepočítávání.

Čili volám metody:

```java
simulation.activateVertex(v);
simulation.deactivateVertex(v); 
```

Poté co všechny události takhle zpracuju, tak provedu vlastní překlopení vrcholů,
které má za následek přepočítání aktivity komponent.

```java
simulation.processVertexActivations();
```

Během přepočítání aktivity komponent, se poznamená, kterých hradel se změna dotkla
a ještě uvnitř metody `processVertexActivations` se všechna tyto hradla strčí
do simulační fronty.

Simulační fronta obsahuje pouze hradla a k nim časy. Prvky neříkají *co* se má udělat,
ale *kdy* se má zkontrolovat stav hradla a zaktualizovat jeho výstupy.

> Tím ušetřím hlídání, zda je tenhle event už naplánovaný nebo ne,
případně ho posunout atd... (zbytečně hodně práce)

Ve frontě může být pro dané hradlo pouze jeden event. Pokud se vkládá znovu, tak se posune.
