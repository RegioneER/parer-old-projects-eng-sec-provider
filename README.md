# Security provider

Fonte template redazione documento:  https://www.makeareadme.com/.


# Descrizione

Il seguente progetto è utilizzato come **dipendenza** interna dal modulo **Cryptolibrary** in cui sono implementate le logiche di verifica di firme digitali dei formati più noti (vedi ETSI https://www.etsi.org/).

# Installazione

Come già specificato nel paragrafo precedente [Descrizione](# Descrizione) si tratta di un progetto di tipo "libreria", quindi un modulo applicativo utilizzato attraverso la definzione della dipendenza Maven secondo lo standard previsto (https://maven.apache.org/): 

```xml
<dependency>
    <groupId>it.eng.parer</groupId>
    <artifactId>eng-sec-provider</artifactId>
    <version>$VERSIONE</version>
</dependency>
```

# Utilizzo

Il modulo definisce modelli di utilità legate alle verifiche di firma digitale.

# Supporto

Mantainer del progetto è [Engineering Ingegneria Informatica S.p.A.](https://www.eng.it/).

# Contributi

Se interessati a crontribuire alla crescita del progetto potete scrivere all'indirizzo email <a href="mailto:areasviluppoparer@regione.emilia-romagna.it">areasviluppoparer@regione.emilia-romagna.it</a>.

# Credits

Progetto di proprietà di [Regione Emilia-Romagna](https://www.regione.emilia-romagna.it/) sviluppato a cura di [Engineering Ingegneria Informatica S.p.A.](https://www.eng.it/).

# Licenza

Questo progetto è rilasciato sotto licenza GNU Affero General Public License v3.0 or later ([LICENSE.txt](LICENSE.txt)).
