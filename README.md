# bodhi
A very minimal di framework in java.
> Supports @Inject for fields,only default/no-args constructor and @Singleton for implementation classes.

## Usage:
```
Injector injector = Bodhi.create(new BindingModule() {
      @Override
      protected void define() {
        bind(Interface.class, Implementation.class);
      }
    });
SomeClassWithInjectedFieldOfInterface instance =  injector.getInstance(SomeClassWithInjectedFieldOfInterface.class);
```

## Dependency
> Available in jcenter repository.

In build.gradle configure jcenter repository.
```
repositories {
    jcenter()
}
```

Then add dependency.
```
compile 'vriksh.bodhi.di:bodhi-di:1.0'
```