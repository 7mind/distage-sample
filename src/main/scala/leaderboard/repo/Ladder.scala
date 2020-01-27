package leaderboard.repo

import distage.DIResource
import izumi.functional.bio.{BIOApplicative, BIOFunctor, BIOPrimitives, BIORef, F}
import leaderboard.model.{QueryFailure, Score, UserId}

trait Ladder[F[_, _]] {
  def submitScore(userId: UserId, score: Score): F[QueryFailure, Unit]
  def getScores: F[QueryFailure, List[(UserId, Score)]]
}

object Ladder {
  final class Dummy[F[+_, +_]: BIOApplicative: BIOPrimitives]
    extends DIResource.Make[F[Throwable, ?], Ladder[F]](
      F.mkRef(Map.empty[UserId, Score]).map(new Dummy.Impl(_))
    )(release = _ => F.unit)

  object Dummy {
    final class Impl[F[+_, +_]: BIOFunctor](
      state: BIORef[F, Map[UserId, Score]],
    ) extends Ladder[F] {
      override def submitScore(userId: UserId, score: Score): F[Nothing, Unit] =
        state.update_(_ + (userId -> score))

      override val getScores: F[Nothing, List[(UserId, Score)]] =
        state.get.map(_.toList.sortBy(_._2)(Ordering[Score].reverse))
    }
  }
}
