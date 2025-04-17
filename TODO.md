
- readme et contributing + commit propre
-
- voir si c'est le comportement qu'on souhaite avoir : should_throw_error_when_user_not_found
- Ajouter les tests front + commit propre
- Ajouter un flag sur le dernier commit pour dire que ce notre base d'application

Peut-etre commencer les dev avant ?
- retourner des objets pou[r gerer les exceptions et ne plus lever d'exception dans le code (ou tres peu) + commit propre
- Ajouter un controle du mp en bdd lors de l'authentification]()
- gérer la durée de la session a l'utilisation ?
- industrialiser la gestion des erreurs coté front : par exemple pour rediriger vers la page de login si la session est expirée (expiration session ou utilisateur plus valide)
- utiliser une bbd sous docker et ajouter au rundev + commit propre
- utiliser un token pas en dure dans la conf https://github.com/settings/tokens

Nice to have
- Username : ne plus demander le username lors de l'authentification mais un objet User ou AuthenticationUser
- EntityBuilder: utiliser une Funtion<T, T> à la place de fieldName
- Configurer module application pour de dependre que de chose utile : runtimeOnly("com.h2database:h2") / implementation(project(":backend:infrastructure"))
  ET voir pour les autre modules aussi
- rundev faire fonctionner la partie front avec le back + ajout au readme + commit propre
- Dependances centralisées avec libs.versions.toml + commit propre
- mettre les fichiers de deploiement separés du reste + 
- 
