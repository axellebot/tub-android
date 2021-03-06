package fr.bourgmapper.tub.domain.interactor;

import com.fernandocejas.arrow.checks.Preconditions;

import javax.inject.Inject;

import fr.bourgmapper.tub.domain.Line;
import fr.bourgmapper.tub.domain.executor.PostExecutionThread;
import fr.bourgmapper.tub.domain.executor.ThreadExecutor;
import fr.bourgmapper.tub.domain.repository.LineRepository;
import io.reactivex.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving data related to an specific {@link Line}.
 */
public class GetLineDetails extends UseCase<Line, GetLineDetails.Params> {

    private final LineRepository lineRepository;

    @Inject
    GetLineDetails(LineRepository lineRepository, ThreadExecutor threadExecutor,
                   PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.lineRepository = lineRepository;
    }

    @Override
    Observable<Line> buildUseCaseObservable(Params params) {
        Preconditions.checkNotNull(params);
        return this.lineRepository.line(params.lineId);
    }

    public static final class Params {

        private final long lineId;

        private Params(long lineId) {
            this.lineId = lineId;
        }

        public static Params forLine(long lineId) {
            return new Params(lineId);
        }
    }
}
