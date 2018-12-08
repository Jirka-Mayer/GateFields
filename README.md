Gate fields
===========

Editor a simulátor logických obvodů.

> Zápočtový program pro Javu - ZS 2018, Jiří Mayer


## Dokumentace

Aplikace je editor a simulátor logických obvodů. Obvody se skládají z elemntárních
prvků `Element`, které jsou propojeny pomocí spojů `Wire`. Spoje vedou vždy mezi dvěma
vrcholy `Vertex`. Vrcholy volně v prostoru jsou označované jako volné `freeVertex`.
Vrcholy náležící k vývodu prvku jsou označované jako vázané `boundVertex`. Schéma obvodu
obsahující všechny prvky reprezentuje třída `Scheme`.


### Editor

Editor je založený na konceptu akcí `Action`. Interakcí s oknem editoru `SchemeView`
vznikají události `Event`, které se předávají třídě `ActionController`. Ten v
sobě drží seznam akcí. Každá akce poslouchá svoji spouštěcí událost. V případě shody
se stane akce aktivní a přebere kontrolu nad editorem. Jsou do ní přesměrovány
všechny události a může editovat schéma obvodu `Scheme`.

Některé akce se samy ukončí v okamžiku spuštění. Mezi takové patří například výběr
prvků nebo jejich mazání. Jiné akce jsou aktivní po delší dobu. Například posun
vybraných prvků. Takové akce jdou většinou potvrdit stiskem enteru, nebo zrušit
pomocí escape. Název aktivní akce se zobrazuje v levém dolním rohu editoru.

Kamerou lze pohybovat táhnutím se stiskem levého tlačítka a zoom se dělá kolečkem myši.

Důležitou částí editoru je výběr objektů. Vybírat lze pouze vrcholy a to pravým
tlačítkem myši. Spoj se stane vybraným, pokud se vyberou oba jeho krajní vrcholy.
Element se stane vybraným, pokud je vybrán alespoň jeden z jeho výstupních vrcholů.

Seznam akcí a jejich spouštěcí událost / klávesa:

| Událost | Akce                                               |
| ------- | -------------------------------------------------- |
| `LMB`   | Pohyb kamery.                                      |
| `WHL`   | Zoom.                                              |
| `RMB`   | Výběr vertexů. `shift` - rozšíření výběru.         |
| `G`     | Posun vybraných prvků. `shift` - spojitý režim.    |
| `R`     | Rotace výběru. `shift` - spojitý režim.            |
| `X`     | Smazání vybraných prvků.                           |
| `W`     | Přidání nebo odebrání spoje mezi dvěma vrcholy.    |
| `E`     | Vytažení spoje z vrcholu (extrude).                |
| `alt+A` | Přidání prvku do schématu (otevře menu).           |
| `T`     | Překlopí stav vstupního prvku (+myš přes něho).    |

Schémata jdou ukládat a načítat do souborů pomocí zkratek `ctrl+S` a `ctrl+O`.

Schéma lze vyčistit a vytvořit nové zkratkou `ctrl+N`.


### Simulátor

Schéma se simuluje během jeho editace. Simulace je tak interaktivní.
Pro ovládání simulátoru existuje pravý postranní panel. Na něm je posuvník nastavující
rychlost simulace a tlačítko pro pozastavení simulace.

Hlavní způsob jak interagovat s obvodem během simulace je pomocí vstupních prvků,
jejichž hodnota lze přepínat klávesou `T` (kurzor musí být přes prvek a focus musí
mít komponenta `SchemeView`).

Signál se šíří skrz spoje nekonečně rychle. Simulátor `Simulator` si udržuje seznam
komponent (vrcholy spojené spoji) a navíc o každém vrcholu ví, zda je aktivní.
Aktivní vrcholy `activeVertex` se chovají jako zdroje signálu a obsahuje-li komponenta
alespoň jeden aktivní vrchol, tak celá komponenta má signál. V případě změny aktivity
vrcholů nebo změně struktury komponent se všechny prvky, kterých se změna dotkla strčí
do fronty pro přepočítání.

Každý prvek se v okamžiku jeho přepočítání podívá na svoje vstupní a výstupní vrcholy
a podle toho které mají signál některé jiné vrcholy aktivuje nebo deaktivuje. Tím se vzruch
šíří skrz schéma.

Každý prvek má nějaké zpoždění, což je čas mezi vyrušením a okamžikem
přepočítání aktivity výstupních vrcholů.

Simulace probíhá v krocích. Každý krok odpovídá nějakému uplynulému času a v každém
kroku se provede přepočítání všech prvků, které v tomto uplynulém čase měly
být přepočítány.
