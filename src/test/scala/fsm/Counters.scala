package fsm

import org.scalacheck.Properties
import org.scalacheck.commands.Commands

import scala.util.{Success, Try}

object CounterCommands extends Commands {
  import org.scalacheck.Gen.{const, oneOf}

  type Sut = Counter
  type State = Long

  override def newSut(state: State) = Counter(state.toInt)

  override def genInitialState = const(0)

  override def genCommand(state: State) = oneOf(Increment, Get)

  override def canCreateNewSut(
    newState: State,
    initSuts: Traversable[State],
    runningSuts: Traversable[Sut]) : Boolean = true

  override def destroySut(sut: Sut): Unit = ()

  override def initialPreCondition(state: State): Boolean = state == 0

  case object Increment extends Command {
    type Result = Int

    override def run(counter: Sut) = counter.increment()

    override def nextState(s: State) = s + 1

    override def preCondition(s: State) = true

    override def postCondition(
      s: State,
      result: Try[Result]) = result == Success(s + 1)
  }

  case object Get extends Command {
    type Result = Int

    override def run(counter: Sut) = counter.get

    override def nextState(state: State) = state

    override def preCondition(state: State) = true

    override def postCondition(
      state: State,
      result: Try[Result]) = result == Success(state)
  }
}

object CounterProperties extends Properties("Counter") {
  property("command sequences") = CounterCommands.property()
}
