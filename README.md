Gate fields
===========

Editor a simulátor logických obvodů.

> Zápočtový program pro Javu - ZS 2018, Jiří Mayer


## Abstraktní zadání

- editace shématu `Scheme`
    - logické prvky `Element`
        - základní hradla (NOT, AND, OR, XOR, NAND)
        - větší prvky kvůli efektivitě simulace / návrhu (RS-LATCH, ...)
        - IO prvky (logical input, diode)
    - spoje `Wire`
        - mohou vést i do "nikam", nebo se větvit
        - počítat s rozšiřitelností (bus, jako skupina spojů)
- simulace
    - diskrétní simulace se zpožděním jednotlivých hradel a spojů
    - možnost pozastavit, upravit rychlost běhu
    - lze editovat schéma během běžící simulace


# Konkrétní implementace editoru

> **Operace, které potřebuju podporovat**
> - pohyb kamery
> - přidávání prvků
> - přidávání spojů
> - udělat výber prvků a spojů
> - pohyb a rotace výběru (diskrétní - 4x)
> - rozpojení spoje od prvku
> - spojení a rozpojení spojů v prostoru mimo prvek
> - více spojů, které se schází na vývodu prvku

- vrchol `Vertex`
    - je konec spoje nebo konec vývodu nějakého prvku
    - pokud se tři spoje sbíhají v jednom místě, toto místo je reprezentováno vertexem
    - vertex označující vývod prvku je svázaný s pozicí prvku
    - spoj vyberu výběrem obou jeho krajních vrcholů
    - prvek vyberu výběrem alespoň jednoho z jeho vývodových vrcholů
    - nikdy nebudu mít volný vrchol bez spoje nebo prvku - ty musím automaticky mazat
    - nikdy nebudu mít dva vrcholy na jednom místě přes sebe - ty automaticky sloučím
- vytvoření prvku
    - někde z menu vyberu prvek, ten se mi přilepí ke kurzoru a čeká se buď na cancel nebo na jeho položení
- vytvoření spoje
    - buď vyberu vrchol vývodu nějakého prvku a "vytáhnu" z něj spoj (klávesa E = extrude)
    - nebo vyberu dva libovolné vrcholy a spjojím je (klávesa M = merge)
- odstranění prvku
    - prvek odstraním jeho výběrem a stiskem klávesy X
    - po prvku zůstanou viset spoje v prostoru
    - (vymaže všechny označené prvky, i spoje mezi označenými prvky)
- odstranění spoje
    - vyberu krajní vrcholy spoje a stisknu Shift + X
    - (vymaže všechny spoje, které jsou označené)
    
> Návrh editoru je hodně inspirovaný aplikací Blender


# Konkrétní implementace simulátoru

- je součástí editoru, dostane pouze ovládací panel
- dikrétní simulace s prioritní frontou
    - ve frontě jsou události
    - když se událost zpracuje, může do fronty zařadit nové události
    - priorita ve frontě odpovídá času konání události
    - z fronty se každý simulační frame odebere tolik událostí, kolik se za ten frame mělo stát
        - každý frame má nějaký reálný čas (může plavat)
        - a také simulační čas (odsud se bere počet zpracovaných událostí)
            - poměr reálného a simulačního času se nastavuje v ovládacím panelu jako rychlost simulace
        - pokud simulace nestíhá, zpomalí se reálný framerate (frame skipping)
            - prostě dokud se neodsimuluje současný frame, tak se další nespustí
                - simlučaní čas se tím jakoby pozastaví
                - indikuje se v ovládacím panelu
- ovládací panel
    - rychlost simulace
    - indikátor frame skippingu
    - pozastavení simulace
