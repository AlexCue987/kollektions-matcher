## About Kollektion-Matchers

* Compare lists, maps, and sets, and produce easy-to-read output - it's especially useful when elements have many fields. Should be used with JUnit5 and kotest.
* No more time-consuming scrolling left and right to figure out what exactly is different.


Comparing collections of short strings or numbers via JUnit and/or Kotest is entirely feasible, and for those there is no need for any improvements.
However, in real life we often need to compare collections of wide objects with multiple fields, and with more elements. This is when we start spending too much time scrolling output like the following test using JUnit, Step3_Lists.whatIsInconvenient():

```
Expected :[SpaceShip(name=Millennium Falcon, description=Han Solo's ship, range=10, capacity=10, serialNumber=123456), SpaceShip(name=Ghost, description=seen in Rogue One, range=10, capacity=10, serialNumber=Unknown), SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123456), SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123356)]
Actual   :[SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457), SpaceShip(name=Millennium Falcon, description=Han Solo's ship, range=10, capacity=10, serialNumber=123456), SpaceShip(name=Ghost, description=seen in Rogue One, range=10, capacity=10, serialNumber=Unknown), SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123456), SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457)]
```

The following output from test Step3_Lists.moreReadableDescription() is easier to read:
```
Mismatch:
actual[0] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457)

Match:
expected[0] == actual[1]: SpaceShip(name=Millennium Falcon, description=Han Solo's ship, range=10, capacity=10, serialNumber=123456)
expected[1] == actual[2]: SpaceShip(name=Ghost, description=seen in Rogue One, range=10, capacity=10, serialNumber=Unknown)
expected[2] == actual[3]: SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123456)

Mismatch:
expected[3] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123356)
actual[4] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457)

Possible matches:
actual[0] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457) is similar to
expected[2] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123456)

"name" = CR90 corvette
"description" = Blockade Runner
"range" = 10
"capacity" = 10
"serialNumber" expected: 123456,
 but was: 123457

actual[0] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457) is similar to
expected[3] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123356)

"name" = CR90 corvette
"description" = Blockade Runner
"range" = 10
"capacity" = 10
"serialNumber" expected: 123356,
 but was: 123457

actual[4] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457) is similar to
expected[2] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123456)

"name" = CR90 corvette
"description" = Blockade Runner
"range" = 10
"capacity" = 10
"serialNumber" expected: 123456,
 but was: 123457

actual[4] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123457) is similar to
expected[3] = SpaceShip(name=CR90 corvette, description=Blockade Runner, range=10, capacity=10, serialNumber=123356)

"name" = CR90 corvette
"description" = Blockade Runner
"range" = 10
"capacity" = 10
"serialNumber" expected: 123356,
 but was: 123457
```

The examples use small collections of short strings, to keep examples terse. They are more useful for longer collections and for classes with multiple fields.

### Compare lists

The following code compares two lists:
```
val expected = listOf(sweetGreenApple, sweetRedApple, sweetGreenPear)
val actual = listOf(sweetGreenPear, sweetGreenApple, sweetRedApple)
matchLists(expected, actual)

Mismatch:
actual[0] = Fruit(name=pear, color=green, taste=sweet)

Match:
expected[0] == actual[1]: Fruit(name=apple, color=green, taste=sweet)
expected[1] == actual[2]: Fruit(name=apple, color=red, taste=sweet)

Mismatch:
expected[2] = Fruit(name=pear, color=green, taste=sweet)

Possible matches:
actual[0] == expected[2], is: Fruit(name=pear, color=green, taste=sweet)
```

In this example `actual[0]` did not have a match in `expected`, so we found a possible match in `expected[2]`. 
In this case it was a complete match. If there are no complete matches, it will find a partial match, as follows:
```
Possible matches:
actual[0] = Fruit(name=onion, color=green, taste=sweet) is similar to
expected[0] = Fruit(name=apple, color=green, taste=sweet)

"name" expected: apple,
 but was: onion
"color" = green
"taste" = sweet
```

We can use a custom lambda to compare list items. For instance, instead of expecting an exact match, we can verify that a double number is in a specified range:
```
        val expected = listOf(1.0, 2.0)
        val actual = listOf(0.91, 2.09)
        assertFailsWith(AssertionError::class, "Guardian Assumption") {
            matchLists(expected, actual)
        }
        matchLists(expected, actual, { a, b -> b in (a - 0.1)..(a + 0.1) } )
```

### Compare maps

The following code compares two maps:
```
        matchMaps(mapOf("Red" to "Apple", "Blue" to "Planet", "White" to "Sugar"),
                mapOf("Red" to "Apple", "Blue" to "Moon", "Amber" to "Light"))

Key in expected, but not in actual:
key=White, value=Sugar

Key in actual, but not in expected:
key=Amber, value=Light

Values mismatch:
key=Blue
  expected=Planet
  actual  =Moon
```

We can use a custom lambda to compare map values. For instance, instead of expecting an exact match, we can verify that a double number is in a specified range:
```
        val expectedMap = mapOf(
                "Morning" to 33.1,
                "Noon" to 61.2,
                "Sunset" to 41.0)
        val actualMap = mapOf(
                "Morning" to 32.9,
                "Noon" to 61.4,
                "Sunset" to 40.6)
        val valuesMatcher: (Any, Any) -> Boolean = { expected, actual -> expected is Double
                && actual is Double
                && abs(expected - actual) < 0.5 }
        matchMaps(expectedMap, actualMap, valuesMatcher)
```

### Compare sets

The following code compares two sets:
```
        matchSets(setOf("Red", "Blue", "White"),
                setOf("Red", "Blue", "Amber"))

Expected, but not in actual:
White

Actual, but not in expected:
Amber
```

* For more examples, have a look at com.tgt.trans.common.testhelpers.example.part1.comparing.collections

### Compare unordered collections

These two collections have an orange and two apples, and differ only in the order of elements, so the following test succeeds:

```kotlin
        matchElementsIgnoringOrder(listOf("apple", "apple", "orange"),
            listOf("apple", "orange", "apple"))
```

These two collection both have some apples and some oranges, but in different quantities, so the test fails:
```kotlin
            matchElementsIgnoringOrder(listOf("apple", "orange"),
                listOf("apple", "orange", "apple"))

Values mismatch:
key=apple
expected=1
actual  =2
```

For instances of data classes with multiple fields, the output includes closest partial matches:
```kotlin
Key in expected, but not in actual:
java.lang.AssertionError: Key in expected, but not in actual:
key=Fruit(name=apple, color=green, taste=sweet), value=1

Key in actual, but not in expected:
key=Fruit(name=apple, color=red, taste=sweet), value=1
key=Fruit(name=pear, color=green, taste=sweet), value=1

Possible Matches:
 expected: Fruit(name=apple, color=green, taste=sweet), 
  but was: Fruit(name=apple, color=red, taste=sweet), 
  distance: 0.67,
  fields:
  name = apple
  color expected: green, but was: red
  taste = sweet

 expected: Fruit(name=apple, color=green, taste=sweet), 
  but was: Fruit(name=pear, color=green, taste=sweet), 
  distance: 0.67,
  fields:
  name expected: apple, but was: pear
  color = green
  taste = sweet
```

## ParallelRunner - reproduce race conditions

Make it easy to reproduce some race conditions, such as the deadlock:

```kotlin
    runInParallel({ runner: ParallelRunner ->
        updateTableA()
        runner.await() //wait until both threads are at this point
        updateTableB()
    },
    { runner: ParallelRunner ->
        updateTableB()
        runner.await() //wait until both threads are at this point
        updateTableC()
    }
)
```

The following example shows how to reproduce a race condition that requires to use a transaction and an isolation level:

```kotlin
    runInParallel(
    { runner: ParallelRunner ->
        val items = readShoppingCart(id = 12345)
        runner.await() 
        val price = computePrice(items)
        runner.await() //wait until new item was added
        savePrice(id = 12345, price = price) // price does not match cart contents
    },
    { runner: ParallelRunner ->
        runner.await() // wait until shopping cart has been read
        addItemToShoppingCart(id = 12345, item = newItem)
        runner.await() 
    }
)
```
In this example `await` is called twice in each thread.