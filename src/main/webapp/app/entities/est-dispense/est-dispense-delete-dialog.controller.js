(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('EstDispenseDeleteController',EstDispenseDeleteController);

    EstDispenseDeleteController.$inject = ['$uibModalInstance', 'entity', 'EstDispense'];

    function EstDispenseDeleteController($uibModalInstance, entity, EstDispense) {
        var vm = this;

        vm.estDispense = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EstDispense.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
