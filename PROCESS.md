

# dag2
- problemen gehad met mijn gradle files door het implementeren van firebase database en firebase authentication. Nu werkend maar nieuwe error

# dag 3
- Eerste gebruiker geregistreerd! Woohoo
- gek iets met logoutknop, nu beetje ghetto gerepareerd (gefixt)
- Nog doen: errors teruggeven als registreren niet lukt
- Besloten om te wachten met de aanbiedingen te filteren op dieet, het zal niet heel moeilijk zijn maar onhandig om vanaf het begin te implementeren. Ook zal het sorteren van de lijst van aanbiedingen gewoon op afstand zijn in eerste geval.
- Toch gekozen om wel direct te werken met dieet en dit te bewerkstelligen door middel van een abstract met dieetwensen.
- Diet toch een losse class gemaakt, moet nog even de database structuur aanpassen

# dag 4
IDEE: maak het mogelijk om een aanbod te maken voor over een paar dagen en ook te filteren daarop.
- Besloten om de offer database te laten werken met push(). Wel daarom User toegevoegd aan de offer.
- TODO overal: eerst checken of er iets in mAuth staat

# dag 5
- Telefoon kapot. Wordt dus lastig debuggen :(
- Address Class gemaakt. Zit in de Offer class.
- Coordinaten aanvragen werkt! via de Geocoding api
- Moet er een soort geschiedenis in?
- OPLETTEN: dat ik de offers filter op dat de datum nog wel moet komen.
- TOEVOEGEN: dat je kan zien wie er mee eet.
- TODO: zorgen dat mensen zich niet op zichzelf kunnen inschrijven
- Ik begin nu wel heel veel classes te krijgen. Is hier iets aan te doen?
- gefixt met klein dingetje. Veel beter.
- TODO: nog even details scherm fixen

# dag 6
- nieuwe telefoon :)
- coordinaten uit de JSON gekregen! 
- na lang zoeken current location gekregen.

# dag 7
- de map in DetaiActivity werkt! (maps voor android api)
- de coordinaten die ik krijg uit geolocations api zijn niet nauwkeurig genoeg, ik woon schijnbaar op het spoor! Ik moet uitzoeken hoe ik het adress zo kan meegeven dat ik nauwkeurigere coordinaten krijg.
-Het lukt nu om de afstand te bereken.
- Moet ik realtime bijhouden of er nog plek is? als iemand op een offer klikt zegmaar
- Er gaat iets mis met de eaters updaten in de firebase.

# dag 8
- van sommige if statements achter elkaar een switch gemaakt
- request functies duidelijkere naam gegeven

# dag 9
- Vandaag ga ik aantal bugs oplossen
*Bugs op dit moment:
  * Krijgt alleen response uit firebase nadat een nieuwe account is aangmaakt 
  * Edit profile activity is kapot (gefixt)
  * Hij laat de kaart niet meer zien :( (gefixt, mega domme fout)
  
- EditProfileActivity laat je huidige profiel instellingen zien


# dag 10:

- er was niet opgeslagen wat ik had getypt :(
- iig een Date toegevoed.
- Hij laat nu een "Hoe vond je het?" knop zien als een offer al is geweest, nog toevoegen dat je er ook voor moet zijn ingeschreven. Das is auch gemacht

# dag 11
- Ik kom er niet uit hoe je uit een static class kunt komen -> opgelost!
- Sorteren op datum lukt! alleen probleem van hierboven nog even fixen
- Review Class aangemaakt en die zit in in de Userclass
- nog zorgen dat iemand zijn eigen offers neit ziet
- Heb het static probleemp opgelost!
- reviews shrijven werkt via een dialog!
- profiel bekijken kan nu ook!

# dag 12
- Nu kun je je adress invullen via een autocomplete van google places api! hierdoor is de geocoding api niet meer nodig. Ook komen de markers nu eindelijk op de goede plek ipv 30 meter meer noordelijk :)

# dag 13
- mooi gemaakt, gecomment en code opgeruimd
