(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('EcoleDeleteController',EcoleDeleteController);

    EcoleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ecole'];

    function EcoleDeleteController($uibModalInstance, entity, Ecole) {
        var vm = this;

        vm.ecole = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Ecole.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
