TestProdCons
============
Les versions_2 sont les versions qui n'utilisent pas la méthode du setDeamon pour tuer les threads consommateurs. A la place,on utilise un système de messages empoisonés qui vont tuer les consommateurs. Ils serons produits par le dernier producteur.