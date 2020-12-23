# datahike-heroku

A clj-template for a web app running on Heroku using datahike with postgres.

## Heroku App Setup

Ensure your newly generated app is backed with a git repo and commit the
initial verison.

Create a heroku app and a postgres database for it (hobby dev is free)

```
heroku apps:create --region eu --addons heroku-postgresql:hobby-dev
```

Make heroku use the latest Clojure CLI version

```
heroku config:set CLOJURE_CLI_VERSION=1.10.1.763
```

Deploy 

```
git push heroku main
```

This will print the URL of you're newly created app at the end.


## What is included?

*) deps.edn used for dependencies
*) plumbing for deployment to Heroku, including a `bin/build` and `Procfile` and using depstar
*) datahike configured to use Heroku postgresql as the storage backend
*) a ring webserver configured to listen on the port provided by Heroku 


## See also

https://devcenter.heroku.com/articles/getting-started-with-clojure

## License

Copyright © 2020 Dieter Komendera

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
