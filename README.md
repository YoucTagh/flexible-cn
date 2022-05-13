# Flexible Content Negotiation

This project include a functional demonstration of Content Negotiation (CN) using profiles that takes the form of SHACL
documents. The implementation was done using Java. Spring Framework is used to handle requests and intercept request
headers. Jena Framework was used to handle RDF graphs and SHACL document and validation. The results will be added soon
to be tested directly in [CNTF](https://w3id.org/cntf) resource.

To run the module for Flexible CN with violation severity checker for example:

* run the `fr.emse.negotiation.contentnegotiation.ContentNegotiationWithSeverityApplication` class.
* make a `GET` request to the end point `http://localhost:8080/fr/emse/kg/negotiation/severity` with an `accept-profile`
  header with the value of one of the three SHACL documents:
    * `https://www.emse.fr/~yousouf.taghzouti/fr/emse/kg/negotiation/h2/shape_graph_1.ttl`
    * `https://www.emse.fr/~yousouf.taghzouti/fr/emse/kg/negotiation/h2/shape_graph_2.ttl`
    * `https://www.emse.fr/~yousouf.taghzouti/fr/emse/kg/negotiation/h2/shape_graph_3.ttl`
    * You can create your own SHACL document and test with it.

Example of a `curl` request:

```shell
curl -H "Accept-profile: https://www.emse.fr/~yousouf.taghzouti/fr/emse/kg/negotiation/h2/shape_graph_1.ttl" http://localhost:8080/fr/emse/kg/negotiation/severity
```

The response would be:

```json
{
  "Data Graph URI": "https://www.emse.fr/~yousouf.taghzouti/fr/emse/kg/negotiation/h2/data_graph_1.ttl",
  "Shape Graph URI": "https://www.emse.fr/~yousouf.taghzouti/fr/emse/kg/negotiation/h2/shape_graph_1.ttl",
  "Validated constraints ": 98,
  "Not Valid constraints Violation": 4,
  "Not Valid constraints Warning": 0,
  "Not Valid constraints Info": 0
}
```

While with:

```shell
curl -H "Accept-profile: https://www.emse.fr/~yousouf.taghzouti/fr/emse/kg/negotiation/h2/shape_graph_1.ttl" http://localhost:8080/fr/emse/kg/negotiation/severity
```


The response is:

```json
{
  "Data Graph URI": "https://www.emse.fr/~yousouf.taghzouti/fr/emse/kg/negotiation/h2/data_graph_3.ttl",
  "Shape Graph URI": "https://www.emse.fr/~yousouf.taghzouti/fr/emse/kg/negotiation/h2/shape_graph_2.ttl",
  "Validated constraints ": 63,
  "Not Valid constraints Violation": 5,
  "Not Valid constraints Warning": 0,
  "Not Valid constraints Info": 0
}
```