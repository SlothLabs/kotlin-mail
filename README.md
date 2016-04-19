# kotlin-mail [![Build Status](https://travis-ci.org/SlothLabs/kotlin-mail.svg?branch=master)](https://travis-ci.org/SlothLabs/kotlin-mail)  [![codecov.io](https://codecov.io/github/SlothLabs/kotlin-mail/coverage.svg?branch=master)](https://codecov.io/github/SlothLabs/kotlin-mail?branch=master)#
## A kotlin-esque wrapper for JavaMail. ##

[JavaMail](https://javamail.java.net/) is the defacto standard library for interacting with email from the Java platform. It's been around for ages (at least 2002, based on what I see in the [JSR docs](https://jcp.org/en/jsr/detail?id=919)), has been used in virtually every Java project requiring email access since then, and is about as bullet proof as you can get by now.

Because of how old it is, it doesn't make any use of any modern Java features (generics, enums, etc.). And because it's so widely used - and used by legacy projects that may not run on more modern JVMs - it isn't likely to get a major facelift any time soon.

Besides, it's *Java*, which if you're looking here is most importantly *not Kotlin*.

(If you some how got here without knowing what Kotlin is, I beseech you to venture to http://kotlinlang.org/. It's a new-ish JVM language from JetBrains, the company who makes the IntelliJ line of IDE's. It's got a lot of awesome features that will make going back to "regular" Java depressing.)

## A Better JavaMail (for now) ##

Because JavaMail is so thoroughly tested, it'd be foolish to just scrap it completely. Right now, the main goal of kotlin-mail is to supplement the JavaMail functionality with stuff that makes it easier to use - at least, from a kotlin perspective. Stuff like:

```kotlin
val connectionInfo = ConnectionInfo(host = "my.imap.host", port = 143, username="test@drive.com", password="12345")

imap(connectionInfo) {
	folder("INBOX", FolderModes.ReadOnly) {
		val results = search {
			+ from("someone@example.com")
			+ subject("Only a Test")
			+ (header("Content-Type", "text/html") or sizeIsAtLeast(1024))
			+ sentOnOrBefore(Date())
		}
		
		results.forEach {
			myEmailProcessor.process(it)
		}
	}
} 
```

Which, in good ol' fashioned JavaMail, looks like:

```java
    final String host = "my.imap.host";
    final String user = "test@drive.com";
    final String password = "12345";
    final int port = 143
 
    final Properties properties = System.getProperties();
    final Session session = Session.getInstance(properties);
    
    Store store;
    Folder inbox;
    try {
	    store = session.getStore("imap");
	    store.connect(host, port, user, password);
	    
	    inbox = store.getFolder("inbox");
	    inbox.open(Folder.READ_ONLY);
	    
	    final FromStringTerm from = new FromStringTerm("someone@example.com");
	    final SubjectTerm subject = new SubjectTerm("Only a Test");
	    
	    final HeaderTerm header = new HeaderTerm("Content-Type", "text/html");
	    final SizeTerm size = new SizeTerm(Comparisons.GE, 1024);
	    final OrTerm headerOrSize = new OrTerm(header, size);
	     
	    final DateTerm date = new DateTerm(Comparisons.LE, new Date());
	    
	    final AndTerm searchTerm = new AndTerm(from, 
	    										new AndTerm(subject,
	    											new AndTerm(headerOrSize, date)
	    										)
	    								 );
	    
	    final Message[] messages = inbox.search(searchTerm);
	    
	    for (final Message msg in messages) {
	    	myEmailProcessor.process(msg);
	    }
	} finally {
		if (inbox != null) {
			try {
				inbox.close(false);
			} catch (MessagingException ex) {
				// we have to catch it, otherwise
				// we won't get to close the 
				// store.
			}
		}
		
		if (store != null) {
			try {
				store.close();
			} catch (MessagingException ex) {
				// don't really have to catch this one,
				// but whatevs.
			}
		}
	}
```

Yeah, not really great :/ Even if you don't take into consideration the exception handling and closing at the end (and neither Store nor Folder implement Closeable/AutoCloseable, so you can't even wrap those in `try-with-resource` blocks), it's still pretty verbose. I mean, it's fine if you're used to working with Java, because that's really the only way to work with it. And you can always roll your own wrappers to make it a little more bearable, like your own connection handling and search building stuff.

But it's still *Java*. We've got Kotlin now, so we get to do cool stuff like type-safe builders, infix functions, and operator overloading, and those can get us some much cleaner code.

## kotlin-mail Goals ##

The first major release - 1.0.0 - is planned to be a full wrapper around the JavaMail library, without really any new bells and whistles applied to it (other than kotlin-izing it). From there, there's at least two main targets in mind.

### Async/Reactive Email ###

It'd be nice to allow a more asynchronous approach to email, where it can handle callbacks or Futures/Promises or whatever as part of the library instead of the standard approach of wrapping it in a general purpose async library. 

### Replace JavaMail ###

Initially this is just going to wrap JavaMail. And maybe it'll just stay that way, but it might be more efficient in the future to replace it completely. In the example above, there's a total of 9 objects created just to handle the search terms. That's not counting anything behind the scenes, any of the overhead of the Folder or Store, any of the messages, etc. I'm sure it'd be possible to somehow represent a complex search term in fewer objects (and I may well be wrong - this is just offhand thoughts as I write this). If we're just wrapping the JavaMail library, we're stuck with doing it the way JavaMail does it.
