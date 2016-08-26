(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('ClasseDetailController', ClasseDetailController);

    ClasseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Classe', 'Inscrire'];

    function ClasseDetailController($scope, $rootScope, $stateParams, previousState, entity, Classe, Inscrire) {
        var vm = this;

        vm.classe = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportcardApp:classeUpdate', function(event, result) {
            vm.classe = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
