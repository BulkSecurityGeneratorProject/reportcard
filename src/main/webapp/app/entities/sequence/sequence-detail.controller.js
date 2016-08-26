(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('SequenceDetailController', SequenceDetailController);

    SequenceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Sequence', 'Evaluation'];

    function SequenceDetailController($scope, $rootScope, $stateParams, previousState, entity, Sequence, Evaluation) {
        var vm = this;

        vm.sequence = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportcardApp:sequenceUpdate', function(event, result) {
            vm.sequence = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
